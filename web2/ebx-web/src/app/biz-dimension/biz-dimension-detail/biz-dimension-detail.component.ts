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
import { GlobalSearchControllerService } from '../../core/search-bar/global-search-controller.service';
import { Constants } from '../../util/constants';
import { Utility } from '../../util/utility';
import { LayoutService } from '../../core/layout.service';

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

  layout: string;
  layoutConfig: any;

  seeAll: boolean;
  pageSize: string;
  textQuery: string;

  constructor(private contentService: ContentService,
              private alert: AlertService,
              private route: ActivatedRoute, 
              private navService: NavigationService,
              private ctService: ContentTemplateService,
              private sbService: SearchBarService,
              private globalSearchCtrl: GlobalSearchControllerService,
              private layoutService: LayoutService,
              private dialog: MatDialog,
              private router: Router) { }

  ngOnInit() {

    // override the route reuse strategy to force component reload
    this.router.routeReuseStrategy.shouldReuseRoute = function() {
        return false;
    };

    var qParams = this.route.snapshot.queryParams;
    let reqLayout = qParams['layout'];
    this.seeAll = qParams['all'] == 'true';

    this.layout = 'default';
    let cgSize = "3";

    this.layoutConfig = this.layoutService.getDimLayoutConfig(reqLayout);

    if (this.layoutConfig) {
      cgSize = this.layoutConfig.pageSize;
      this.layout = this.layoutConfig.type;
    }

    if (this.seeAll) {
      cgSize = '50';
    }

    this.pageSize = cgSize;

    this.route.params.subscribe(params => {
      let cQId = params['cQId'];
      let recIdentity = params['identity'];

      let filters = this.sbService.buildFiltersFromQueryParams(qParams);
      let childQIds = filters['cqid'];
      this.textQuery = this.sbService.getTextQueryFromQueryParams(qParams);
      var atCtxParams = Utility.readTrackingCtxInQueryParams(this.route.snapshot.queryParams);

      this.contentService.getRecordAndChildData(cQId, recIdentity, '0', cgSize, 
                Constants.AT_CHANNEL_WEB, childQIds, this.textQuery, atCtxParams).subscribe(
        res => {

          var childContainerWithData = [];
          for (var i = 0; i < res.length; i++) {

            let contentGrp = res[i];

            let container = this.ctService.getContainerByQId(contentGrp.contentQId);
            if (container.linkContainerQId) {
              contentGrp.linkContainer = container;
              childContainerWithData.push(container.qualifiedId);
              container = this.ctService.getContainerByQId(container.linkContainerQId);
            } else {
              childContainerWithData.push(container.qualifiedId);
            }

            if (contentGrp.contentQId == cQId) {
              
              this.record = contentGrp.records.content[0];
              this.container = container;
              
            }

            contentGrp.container = container;
            contentGrp.records.content.forEach(rec => {
              this.contentService.decorateRecord(container, rec);
            });
          }

          let filterIds = this.sbService.getFilterIdsFromQueryParams(qParams);
          this.globalSearchCtrl.setBizDimDetailSearchBar(this.container, null, this.record, filterIds, this.textQuery);

          this.children = res.filter(cg => cg.contentQId != cQId);
          this.noChildren = (!this.children || this.children.length == 0);
        }
      );
    });

  }

  addNextDataPage(cg: ContentRecordGroup) {

    var childQIds = [cg.container.qualifiedId];

    this.contentService.getRecordAndChildData(this.container.qualifiedId, this.record.identity, 
              String(cg.records.number+1), this.pageSize, Constants.AT_CHANNEL_WEB, childQIds, this.textQuery).subscribe(
      res => {

        for (var i = 0; i < res.length; i++) {

          let contentGrp = res[i];
          if (cg.contentQId == contentGrp.contentQId) {
            contentGrp.records.content.forEach(rec => {
              this.contentService.decorateRecord(cg.container, rec);
              cg.records.content.push(rec);
            });

            cg.records.first = contentGrp.records.first;
            cg.records.last = contentGrp.records.last;
            cg.records.number = contentGrp.records.number;
            cg.records.numberOfElements = contentGrp.records.numberOfElements;

            break;
          }
        }

      }
    );
  }

  ngAfterViewInit(): void {
    if (this.deleteButton) {
      this.deleteButton.onDelete.subscribe(res => {
          if (res) {
            this.goBackHome();
          }
        });
    }
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
    let paramId = "sf_cqid";
    let queryParams: { [key: string] : string } = {};
    queryParams[paramId] = contentGrp.container.qualifiedId;
    queryParams['all'] = 'true';
    
    var textQuery = this.route.snapshot.queryParams['tq'];
    if (textQuery) {
      queryParams['tq'] = textQuery;
    }

    this.navService.goToDimDetail(this.container.qualifiedId, this.record.identity, queryParams);
  }

  goBackHome() {
    this.navService.goToPortalHome();
  }

}
