import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter } from '@angular/core';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { ContentTemplateService } from '../../core/content-template.service';
import { Utility } from '../../util/utility';

@Component({
  selector: 'ebx-content-stack-preview',
  templateUrl: './content-stack-preview.component.html',
  styleUrls: ['./content-stack-preview.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentStackPreviewComponent implements OnInit {

  _record: any;
  @Input() container: Container;
  @Input() editing: boolean;

  @Output() onStackUpdate = new EventEmitter<any>();

  showCount: number = 5;

  stackArr: any;
  contentStack: any;
  heading: string;

  lnkContainerContent: any[];

  linkageChange: any = {};

  @Input() set record(rec: any) {
    this._record = rec;
    this.initStackInfo();
  }

  get record() {
    return this._record
  }

  constructor(private contentService: ContentService,
    private alert: AlertService,
    private ctService: ContentTemplateService) { }

  ngOnInit() {

    if (this.container.contentStackItemId) {
      var stackQId = Utility.getQId(this.container.qualifiedId, this.container.contentStackItemId);
      var cItem = this.ctService.templateCache.contentStackQIdMap[stackQId];  
      if (cItem) {
        this.heading = cItem.label;
      }
    }
    
  }

  initStackInfo() {
    this.stackArr = this.record[this.container.contentStackItemId];
    this.linkageChange = {};

    this.fetchStackDetails();
    this.fetchLinkedContent();
  }

  fetchLinkedContent() {
    
    if (this.container.linkedBizContent) {
      
      this.contentService.getRecordAndChildData(this.container.qualifiedId, this._record.identity, "0", "50")
          .subscribe(
              res => {

                var contentGrpMap = {};

                for (var i = 0; i < res.length; i++) {

                  let contentGrp = res[i];

                  let container = this.ctService.getContainerByQId(contentGrp.contentQId);
                  if (container.linkContainerQId) {
                    contentGrp.linkContainer = container;
                    container = this.ctService.getContainerByQId(container.linkContainerQId);
                  }

                  if (contentGrp.contentQId !== this.container.qualifiedId) {
                    contentGrp.container = container;
                    contentGrp.records.content.forEach(rec => {
                      this.contentService.decorateRecord(container, rec);
                    });
                    contentGrp.count = contentGrp.records ? contentGrp.records.totalElements : 0;
                    contentGrpMap[contentGrp.contentQId] = contentGrp;
                  }
                }

                this.lnkContainerContent = [];
                this.container.linkedBizContent.forEach((lc) => {
                  var cg = contentGrpMap[lc.qualifiedId];
                  if (cg) {
                    this.lnkContainerContent.push(cg);
                  } else {
                    var cntnr = this.ctService.getConcreteContainerByQId(lc.linkContainerQId);
                    this.lnkContainerContent.push({
                      linkContainer: lc,
                      container: cntnr,
                      count: 0
                    });
                  }
                });

              }
            );
    }
  }

  convertToBrowserItems(cg: any) {
    
    var browserItems = [];
    
    if (cg.records && cg.records.content) {
      cg.records.content.forEach((c) => {
        browserItems.push({
          identity: c.identity,
          label: c.__title,
          qualifiedId: cg.container.qualifiedId,
          containerLabel: cg.container.label
        });
      });
    }

    if (cg.newRecords) {
      cg.newRecords.forEach((nr) => { browserItems.push(nr); });
    }

    return browserItems;
  }

  linkedItemSelectionDone(selection: any, cg: any) {
    var change = {};
    selection.forEach((sc) => {
      change[sc.identity] = {
        item: sc,
        status: "NEW" // NEW
      };
    });

    var lnkChange: any = {
        linkContainerQId: cg.linkContainer.qualifiedId,
        newLinks: [],
        removedLinks: []
      };

    if (cg.records && cg.records.content) {
      // check removed items
      cg.records.content.forEach((c) => {
        
        var chngEntry = change[c.identity];
        
        if (chngEntry) {
          
          chngEntry.status = "OLD"
          c.__removed = false;

        } else {
        
          change[c.identity] = {
            item: {
              identity: c.identity,
              label: c.__title,
              qualifiedId: cg.container.qualifiedId,
              containerLabel: cg.container.label
            },
            status: "DEL"
          }

          c.__removed = true;
        }

      });
    }

    var newItems = [];

    for (let id in change) {
      
      var chngEntry = change[id];

      if (chngEntry) {
      
        if (chngEntry.status == "NEW") {
          newItems.push(chngEntry.item);
          lnkChange.newLinks.push(chngEntry.item);
        } else if (chngEntry.status == "DEL") {
          lnkChange.removedLinks.push(chngEntry.item);
        }

      } 
    }

    cg.newRecords = newItems;
    cg.count = selection.length;

    this.linkageChange[cg.linkContainer.qualifiedId] = lnkChange;
    
    this._record.__linkageChange = [];
    for (let key in this.linkageChange) {
      this._record.__linkageChange.push(this.linkageChange[key]);
    }
  }

  fetchStackDetails() {
    if (this.stackArr) {
      this.contentService.getContentStackDetails(this.stackArr).subscribe(
        (result: any) => {
          this.contentService.decorateContentGroup(result);
          this.contentStack = result;
        }, 
        (error: any) => {
          this.alert.error("Error fetch asset details", error.statusCode);
        }
      );
    }
  }

  selectionDone(stackSelection: any) {
    if (stackSelection) {
      this.stackArr = stackSelection;
      this.record[this.container.contentStackItemId] = this.stackArr;
      this.fetchStackDetails();
      this.onStackUpdate.emit();
    }
  }

}
