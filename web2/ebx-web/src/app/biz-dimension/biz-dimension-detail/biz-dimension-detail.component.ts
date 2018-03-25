import { Component, OnInit, ViewEncapsulation, ViewChild, QueryList, AfterViewInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { ContentRecordGroup } from '../../model/content-record-group.model';
import { ContentTemplateService } from '../../core/content-template.service';
import { NavigationService } from '../../app-routing/navigation.service';
import { ContentDeleteButtonComponent } from '../../content-action/content-delete-button/content-delete-button.component';
import { EditBizDimensionComponent } from '../../biz-dimension/edit-biz-dimension/edit-biz-dimension.component';
import { SearchBarService } from '../../core/search-bar/search-bar.service';

@Component({
  selector: 'ebx-biz-dimension-detail',
  templateUrl: './biz-dimension-detail.component.html',
  styleUrls: ['./biz-dimension-detail.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class BizDimensionDetailComponent implements OnInit, AfterViewInit {

  @ViewChild(ContentDeleteButtonComponent) deleteButton: ContentDeleteButtonComponent;

  record: any;
  container: Container;
  children: ContentRecordGroup[] = [];
  noChildren: boolean = false;

  constructor(private contentService: ContentService,
              private alert: AlertService,
              private route: ActivatedRoute, 
              private navService: NavigationService,
              private ctService: ContentTemplateService,
              private sbService: SearchBarService,
              private dialog: MatDialog) { }

  ngOnInit() {

    this.route.params.subscribe(params => {
      let cQId = params['cQId'];
      let recIdentity = params['identity'];

      this.contentService.getRecordAndChildData(cQId, recIdentity, "3")
          .subscribe(
              res => {

                for (var i = 0; i < res.length; i++) {

                  let contentGrp = res[i];

                  let container = this.ctService.getContainerByQId(contentGrp.contentQId);
                  if (container.linkContainerQId) {
                    contentGrp.linkContainer = container;
                    container = this.ctService.getContainerByQId(container.linkContainerQId);
                  }

                  if (contentGrp.contentQId == cQId) {
                    this.record = contentGrp.records.content[0];
                    this.container = container;
                    this.sbService.setBizDimDetailSearchBar(container, this.record, []);
                  }

                  contentGrp.container = container;
                  contentGrp.records.content.forEach(rec => {
                    this.contentService.decorateRecord(container, rec);
                  });
                }

                this.children = res.filter(cg => cg.contentQId != cQId);
                this.noChildren = (!this.children || this.children.length == 0);
              }
            );
    });

  }

  ngAfterViewInit(): void {
    this.deleteButton.onDelete.subscribe(res => {
        if (res) {
          this.goBackHome();
        }
      });
  }

  editState() {

    let dialogRef = this.dialog.open(EditBizDimensionComponent, {
        width: '624px',
        disableClose: true,
        data: { 
          containerQId: this.container.qualifiedId, 
          newRecord: false, recordIdentity: 
          this.record.identity 
        }
      });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.contentService.getContentRecord(this.container.qualifiedId, this.record.identity)
            .subscribe(res => {
              this.record = res;
              this.contentService.decorateRecord(this.container, this.record);
            }, err => {
              this.alert.error('Error in fetching record details.', err.status);
            })
      }
    });
  }

  navToAllContent(contentGrp: ContentRecordGroup) {
    let paramId = "sf_" + contentGrp.linkContainer.linkContentItemId;
    let queryParams: { [key: string] : string } = {};
    queryParams[paramId] = this.record.identity;
    this.navService.goToContentList(contentGrp.container.qualifiedId, queryParams);
  }

  goBackHome() {
    this.navService.goToPortalHome();
  }

}
