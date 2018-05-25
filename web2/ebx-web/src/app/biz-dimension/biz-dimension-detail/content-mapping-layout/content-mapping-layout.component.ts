import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { ContentRecordGroup } from '../../../model/content-record-group.model';
import { ContentConnService } from '../../../core/content-conn.service';
import { AlertService } from '../../../core/alert/alert.service';
import { ContentTemplateService } from '../../../core/content-template.service';

@Component({
  selector: 'ebx-content-mapping-layout',
  templateUrl: './content-mapping-layout.component.html',
  styleUrls: ['./content-mapping-layout.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ContentMappingLayoutComponent implements OnInit {


  _contentGroups: ContentRecordGroup[];

  @Input() config: any;

  contentMappings: string;
  mappedCGs: any;

  groupedRecords: any;
  showCount = 5;

  constructor(private contentConnService: ContentConnService,
    private alert: AlertService, private ctService: ContentTemplateService) { }

  @Input() 
  set contentGroups(cgs: ContentRecordGroup[]) {
    
    this._contentGroups = cgs;
    this.mappedCGs = [];
    
    cgs.forEach((cg) => {
      let container = this.ctService.getConcreteContainerByQId(cg.contentQId);  
      this.mappedCGs[container.qualifiedId] = cg.records;
    });
  }

  get contentGroups() {
    return this._contentGroups;
  }

  ngOnInit() {
    this.contentConnService.getFirstContentConnVOByContentQId(this.config.groupQId).subscribe(
      (res: any) => {
        
        this.groupedRecords = [];
        
        if (res != null) { 
          
          var i = 0;
          res.valueLinks.forEach((vl) => {
            
            var grp = {
              id: "cm-" + vl.recordIdentity,
              identity: vl.recordIdentity,
              title: vl.recordTitle,
              records: [],
              desc: this.getDescription(vl),
              active: (i == 0)
            }

            if (vl.connectedContainers) {
              vl.connectedContainers.forEach((cc) => {
                var recPage = this.mappedCGs[cc];
                if (recPage) {
                  recPage.content.forEach((rec) => {
                    grp.records.push({
                      containerQId: cc,
                      record: rec
                    });
                  });
                }
              });
            }

            this.groupedRecords.push(grp);
            i++;
            
          });

        }
      },
      (err) => {
        this.alert.error("Error fetching mapping. Please try later.", err.statusCode);
      }
    )
  }

  getDescription(vl: any) {
    var desc = ""
    if (vl.connectedContainers) {
      for (var i = 0; i < vl.connectedContainers.length; i++) {
        var cQId = vl.connectedContainers[i];
        var cont = this.ctService.getContainerByQId(cQId);
        if (cont) {
          if (i > 0) {
            desc += ", "
          }
          desc += cont.label; 
        }
      }
    }
    return desc;
  }

  selectGroup(cg: any) {
    
    this.groupedRecords.forEach((grp) => {
      grp.active = false;
    });

    cg.active = true;

    var elem = document.getElementById(cg.id);
    if (elem) {
      elem.scrollIntoView();
    }
  }

}
