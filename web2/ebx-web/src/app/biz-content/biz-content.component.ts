import { Optional, Component, OnInit, ViewEncapsulation, Input, ViewChildren, AfterViewInit, QueryList, Inject } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { Location } from '@angular/common';

import { AppContext } from '../app-context';
import { Container } from '../model/container.model';
import { ContentItem } from '../model/content-item.model';
import { ContentService } from '../core/content/content.service';
import { ContentTemplateService } from '../core/content-template.service'; 
import { AlertService } from '../core/alert/alert.service';
import { UserService } from '../core/auth/user.service';
import { NavigationService } from '../app-routing/navigation.service';
import { ConfirmDialogComponent } from '../core/confirm-dialog/confirm-dialog.component'; 
import { ContentDeleteButtonComponent } from '../content-action/content-delete-button/content-delete-button.component';
import { ContentReqApproveButtonComponent } from '../content-action/content-req-approve-button/content-req-approve-button.component';
import { ContentReqRejectButtonComponent } from '../content-action/content-req-reject-button/content-req-reject-button.component';
import { ContentWorkflowService } from '../services/content-workflow.service';
import { ContentPreviewService } from '../core/content/content-preview.service';
import { Constants } from '../util/constants';
import { Utility } from '../util/utility';
import { Permissions } from '../model/permissions.model';

@Component({
  selector: 'ebx-biz-content',
  templateUrl: './biz-content.component.html',
  styleUrls: ['./biz-content.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class BizContentComponent implements OnInit, AfterViewInit {

  @ViewChildren(ContentDeleteButtonComponent) deleteButtons: QueryList<ContentDeleteButtonComponent>;
  @ViewChildren(ContentReqApproveButtonComponent) approveBtns: QueryList<ContentReqApproveButtonComponent>;
  @ViewChildren(ContentReqRejectButtonComponent) rejectBtns: QueryList<ContentReqRejectButtonComponent>;

  appCtx = AppContext;

  @Input() record?: any;
  @Input() container?: Container;
  @Input() editing?: boolean = false;

  recordBackup: any;

  isNewRec: boolean = false;
  approvalWFRequired: boolean = false;
  isDraft: boolean = false;

  contentRequest: any;

  private sub: any;

  private qualityAlerts: any[];

  inputItems: ContentItem[];
  boundedItems: ContentItem[];
  title: string;

  enableEditAction = true;
  enableDelAction = true;

  enableApproveAction = false;
  enableRejectAction = false;

  returnUrl: string;

  recordNotFound: boolean = false;
  errors: any = {};

  crAction: string;

  constructor(private route: ActivatedRoute, private contentService: ContentService,
              private alert: AlertService, private contentTemplate: ContentTemplateService,
              private contentWFService: ContentWorkflowService, private loc: Location,
              private contentPreviewService: ContentPreviewService,
              private user: UserService,
              private navService: NavigationService, private dialog: MatDialog,
              private router: Router, @Optional() @Inject(MAT_DIALOG_DATA) public data?: any,
              @Optional() public dialogRef?: MatDialogRef<BizContentComponent>,) { }

  ngOnInit() {
    // override the route reuse strategy to force component reload
    this.router.routeReuseStrategy.shouldReuseRoute = function() {
        return false;
    };

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    this.approvalWFRequired = this.contentWFService.isApprovalWFRequired();

    if (this.data) {
      this.record = this.data.record;
      this.container = this.data.container;
      this.editing = this.data.editing;
    }

    if (!this.record) {
      
      this.route.params.subscribe(params => {
          
          let containerQId = params['cQId'];
          let recordIdentity = params['identity'];

          if (containerQId && recordIdentity) {
            
            this.container = this.contentTemplate.getConcreteContainerByQId(containerQId);
            var atCtxParams = Utility.readTrackingCtxInQueryParams(this.route.snapshot.queryParams);

            this.contentService.getContentRecord(containerQId, recordIdentity, AppContext.channel, atCtxParams)
                  .subscribe(
                      result => {
                        
                        this.record = result;
                        
                        if (this.record.identity) {
                          this.contentService.decorateRecord(this.container, this.record);
                          this.initState();  
                        } else {
                          this.record = null;
                          this.recordNotFound = true;
                        }
                        
                      }, 
                      error => {
                        this.alert.error("Error fetching record details", error.status);
                      }
                    );
            
          } else {

            let crIdentity = params['crIdentity'];
            
            if (crIdentity) {

              this.contentWFService.getContentRequest(crIdentity, AppContext.channel, atCtxParams).subscribe((res: any) => {
                this.contentRequest = res;
                this.container = this.contentTemplate.getConcreteContainerByQId(res.objectRef.contentQId);
                this.initContentRequest();
              }, err => {
                this.alert.error("Error fetching record details", err.status);
              });
            }
          }

      });

    } else {
      this.contentService.decorateRecord(this.container, this.record);
      this.initState();
    }

  }

  initContentRequest() {

    this.record = this.contentRequest.objectRef.data;
    this.isDraft = this.contentRequest.currentState.stateName == 'DRAFT';

    this.enableApproveAction = this.contentWFService.isActionAllowed(
      ContentWorkflowService.ACTION_APPROVE, this.contentRequest);

    this.enableRejectAction = this.contentWFService.isActionAllowed(
      ContentWorkflowService.ACTION_REJECT, this.contentRequest);

    this.enableEditAction = this.contentWFService.isActionAllowed(
      ContentWorkflowService.ACTION_EDIT, this.contentRequest);

    this.enableDelAction = this.contentWFService.isActionAllowed(
      ContentWorkflowService.ACTION_DISCARD, this.contentRequest);
    
    this.contentService.decorateRecord(this.container, this.record);
    this.initState();
    
  }

  reloadContentRecord(success: boolean) {
    if (success && !this.contentRequest) {
      this.navService.goToContentDetail(
          this.container.qualifiedId, this.record.identity, {}, this.returnUrl);
    }
  }
  
  updateRecordLocalCopy(rec: any) {
    this.record = rec;
    this.recordBackup = JSON.parse(JSON.stringify(this.record));
  }

  hasValue(val: any) {
    return this.contentService.attrHasValue(val);
  }

  ngAfterViewInit(): void {
    this.deleteButtons.changes.subscribe((comps: QueryList <ContentDeleteButtonComponent>) => {
      if (comps.first) {
        comps.first.onDelete.subscribe(res => {
          if (res) {
            this.goBackHome();
          }
        });
      }
    });

    var crAction = this.route.snapshot.params['action'];
    
    if (crAction) {
      
      if (crAction === 'approve') {
        
        this.approveBtns.changes.subscribe((comps: QueryList <ContentReqApproveButtonComponent>) => {
          if (comps.first && this.enableApproveAction) {
            comps.first.approveRecord();
          }
        });

      } else if (crAction === 'reject') {
        
        this.rejectBtns.changes.subscribe((comps: QueryList <ContentReqRejectButtonComponent>) => {
          if (comps.first && this.enableRejectAction) {
            comps.first.rejectRecord();
          }
        });
      }

    }
  }

  private initState() {

    if (this.container) {
    
      var previewHandler = this.contentPreviewService.getPreviewHandler(this.record);
      var previewType = previewHandler ? previewHandler.type() : null;

      this.inputItems = 
          this.contentTemplate.templateCache.getFreeInputContentItems(this.container)
              .filter(item => this.container.titleItemId != item.id && 
                              (previewType !== 'text' || 
                                this.container.textItemId != item.id) );

      this.boundedItems = this.container.contentItem.filter(item => this.isRelevanceItem(item));
    }

    if (this.record) {
      this.isNewRec = !this.record.identity && (!this.contentRequest);
      this.updateRecordLocalCopy(this.record);
      if (this.record.archived && !this.contentRequest) {
        this.enableEditAction = false;
        this.enableDelAction = false || this.user.userHasPermission(Permissions.DELETE_ARCHIVED_CONTENT);
      }
    }
  }

  isRelevanceItem(item: ContentItem) {
    return this.contentTemplate.templateCache.isRelevanceItem(item);
  }

  checkErrors() {
    
    var hasErrors = false;
    
    var title = this.record[this.container.titleItemId];
    if (!title || title.trim().length == 0) {
      this.errors.title = { required: true };
      hasErrors = true;
    }

    return hasErrors;
  }

  onTitleChange() {
    var title = this.record[this.container.titleItemId];
    if (title.trim().length > 0) {
      this.errors.title = null;
    } else {
      this.errors.title = { required: true };
    }
  }

  onContentUpdate() {
    this.contentService.decorateRecord(this.container, this.record);
    this.record = JSON.parse(JSON.stringify(this.record));
  }

  publishContent() {

    if (this.checkErrors()) {
      return;
    }

    if (this.isDraft) {

      this.publishWFContent();

    } else {

      if (this.contentRequest) {
        
        this.submitWFContent(false, true);

      } else if (this.approvalWFRequired) {

        this.submitWFContent(false, false);

      } else {

        this.contentService.saveContainerData(this.container.qualifiedId, this.record)
          .subscribe(
              result => {
                
                this.editing = false;
                this.contentService.decorateRecord(this.container, result.contentRecord);
                this.record = result.contentRecord;

                let successMsg = this.isNewRec ? "Content Asset added successfully." :
                                  "Content Asset updated successfully.";
                                  
                this.alert.success(successMsg, true);

                if (this.dialogRef) {
                  this.dialogRef.close(this.record);
                } else {
                  this.navService.goToContentDetail(
                    this.container.qualifiedId, result.contentRecord.identity, {});
                }

              },
              error => {
                if (error.qualityAlerts) {
                  this.qualityAlerts = error.qualityAlerts;
                } else {
                  this.alert.error("Error saving data.", error.status);  
                }
              }
            );
      }
    }
  }

  goToContentDetailForRequest(res: any) {
    if (this.dialogRef) {
      this.dialogRef.close(res);
    } else {
      this.navService.goToContentDetail(
        this.container.qualifiedId, res.objectRef.data.identity, {}, this.returnUrl);
    }
  }

  onReject(res: any) {
    this.processWFResponse(res);
  }

  submitWFContent(draftSave: boolean, editContentRequest: boolean = false) {
    
    if ((this.isDraft && draftSave) || editContentRequest) {
      // record edit action
      this.contentWFService.editContentRequest(this.contentRequest.objectRef.identity, this.container.qualifiedId, this.record, null).subscribe((res: any) => {
        this.alert.success("Saved successfully!");
        this.editing = false;
        this.processWFResponse(res);
      }, error => {
        this.alert.error("Error saving data.", error.status);  
      });

    } else {

      this.contentWFService.submitContent(this.container.qualifiedId, this.record, null, draftSave, null).subscribe((res: any) => {
        let alertMsg = draftSave ? "Saved successfully." :
            "Content Asset Add/Update request has been submitted for approval. The asset will be published once approved. And you will receive notification on approval.";
        this.alert.success(alertMsg, true);
        this.editing = false;
        this.processWFResponse(res);
      }, error => {
        this.alert.error("Error saving data.", error.status);  
      });
    }
  }

  processWFResponse(res: any) {
    
    this.contentRequest = res;

    this.initContentRequest();

    if (this.dialogRef) {
      this.dialogRef.close(res);
    } else {
      this.navService.goToContentRequestDetail(res.objectRef.identity);
    }
  }

  publishWFContent() {
    this.contentWFService.publishContentRequest(this.contentRequest.objectRef.identity, this.container.qualifiedId, this.record, null).subscribe((res: any) => {

      this.alert.success("Published successfully!");

      if (this.approvalWFRequired) {
        this.editing = false;
        this.processWFResponse(res);
      } else {
        this.goToContentDetailForRequest(res);  
      }
      
    }, error => {
      this.alert.error("Error publishing data.", error.status);  
    });
  }

  goBackHome() {
    if (this.dialogRef) {
      this.dialogRef.close();
    } else {
      this.router.navigateByUrl(this.returnUrl);
    }
  }

  editState() {
    this.editing = true;
  }

  getDeleteConfirmText() : string {
    return "You are about to delete '" + this.record.__title 
      + "' asset.<br>You won't be able to restore it. Would you like to proceed?";
  }

  deletePrompt() {
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
        width: '624px',
        disableClose: true,
        data: { 
          title: "Delete Asset",
          text: this.getDeleteConfirmText(),
          confirmLabel: "Delete Asset",
          cancelLabel: "No, keep it."
        }
      });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.deleteRecord();
      }
    })
  }

  deleteRecord() {
    this.contentService.deleteContentRecord(this.container.qualifiedId, this.record.identity).subscribe(res => {
      this.alert.success("Deleted successfully.");
      this.goBackHome();
    }, err => {
      this.alert.error("Unable to delete. Please try later.", err.status);
    });
  }

  saveDraft() {
    
    if (this.checkErrors()) {
      return;
    }

    this.submitWFContent(true);
  }

  cancelEdit() {
    this.errors = {};
    if (this.isNewRec) {
      this.goBackHome();
    }
    this.editing = false;
    this.updateRecordLocalCopy(this.recordBackup);
  }

  getContentRequestStatusText() {
    if (this.contentRequest) {
      if (this.contentRequest.actionHistory.actions && this.contentRequest.actionHistory.actions.length > 0) {
        var lastAction = this.contentRequest.actionHistory.actions[this.contentRequest.actionHistory.actions.length - 1];
        let actionName = this.contentWFService.actionDisplayText[lastAction.actionName];
        return actionName + " by " + lastAction.actorName;
      }
    }
    return '';
  }

  getContentRequestTypeText() {
    if (this.contentRequest) {
      if (this.contentRequest.objectRef.requestType) {
        var text = this.contentWFService.requestTypeDisplayText[this.contentRequest.objectRef.requestType];
        return text || this.contentRequest.objectRef.requestType;
      }
    }
    return '';
  }

  deleteDoc() {
    if (this.record && this.record.__decoration.__docMetadata) {
      delete this.record.__decoration.__docMetadata;
      var docFld = this.record[this.container.docItemId];
      docFld.deleted = true;
    }
  }

  deleteThumbnail() {
    if (this.record && this.record.__decoration && this.record.__decoration.__thumbnailDoc) {
      delete this.record.__decoration.__thumbnailDoc;
      this.record.__thumbnailDoc.deleted = true;
    }
  }

  navToHome() {
    this.navService.goToPortalHome();
  }

  ngOnDestroy() {
    if (this.sub) {
      this.sub.unsubscribe();  
    }
  }

}

export class RecordAction {
  id: string;
  label: string;
  styleClass: string;
}
