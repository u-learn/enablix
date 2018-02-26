import { Component, OnInit, ViewEncapsulation, Input, ViewChildren, AfterViewInit, QueryList } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material';
import { Location } from '@angular/common';

import { Container } from '../model/container.model';
import { ContentItem } from '../model/content-item.model';
import { ContentService } from '../core/content/content.service';
import { ContentTemplateService } from '../core/content-template.service'; 
import { AlertService } from '../core/alert/alert.service';
import { NavigationService } from '../app-routing/navigation.service';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component'; 
import { ContentDeleteButtonComponent } from '../content-action/content-delete-button/content-delete-button.component';
import { ContentWorkflowService } from '../services/content-workflow.service';

@Component({
  selector: 'ebx-biz-content',
  templateUrl: './biz-content.component.html',
  styleUrls: ['./biz-content.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class BizContentComponent implements OnInit, AfterViewInit {

  @ViewChildren(ContentDeleteButtonComponent) deleteButtons: QueryList<ContentDeleteButtonComponent>;

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

  constructor(private route: ActivatedRoute, private contentService: ContentService,
              private alert: AlertService, private contentTemplate: ContentTemplateService,
              private contentWFService: ContentWorkflowService, private loc: Location,
              private navService: NavigationService, private dialog: MatDialog,
              private router: Router) { }

  ngOnInit() {

    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

    this.approvalWFRequired = this.contentWFService.isApprovalWFRequired();

    if (!this.record) {
      
      this.route.params.subscribe(params => {
          
          let containerQId = params['cQId'];
          let recordIdentity = params['identity'];

          if (containerQId && recordIdentity) {
            
            this.container = this.contentTemplate.getConcreteContainerByQId(containerQId);

            this.contentService.getContentRecord(containerQId, recordIdentity)
                  .subscribe(
                      result => {
                        this.record = result;
                        this.contentService.decorateRecord(this.container, this.record);
                        this.initState();
                      }, 
                      error => {
                        console.error("Error getting record: " + error);
                        this.alert.error("Error fetching record details", error.status);
                      }
                    );
            
          } else {

            let crIdentity = params['crIdentity'];
            
            if (crIdentity) {

              this.contentWFService.getContentRequest(crIdentity).subscribe((res: any) => {
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
  }

  private initState() {

    if (this.container) {
    
      this.inputItems = 
          this.contentTemplate.templateCache.getFreeInputContentItems(this.container)
              .filter(item => this.container.titleItemId != item.id && 
                              (this.record.__decoration.__thumbnailUrl || 
                                this.container.textItemId != item.id) );

      this.boundedItems = this.container.contentItem.filter(item => item.bounded);
    }

    if (this.record) {
      this.isNewRec = !this.record.identity && (!this.contentRequest);
      this.updateRecordLocalCopy(this.record);
    }
  }

  onContentUpdate() {
    this.contentService.decorateRecord(this.container, this.record);
    this.record = JSON.parse(JSON.stringify(this.record));
  }

  publishContent() {

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
                this.navService.goToContentDetail(
                  this.container.qualifiedId, result.contentRecord.identity);
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
    this.navService.goToContentDetail(
      this.container.qualifiedId, res.objectRef.data.identity);
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
      this.contentWFService.submitContent(this.container.qualifiedId, this.record, draftSave, null).subscribe((res: any) => {
        this.alert.success("Saved successfully!");
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
    this.navService.goToContentRequestDetail(res.objectRef.identity);
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
    //this.navService.goToPortalHome();
    this.router.navigateByUrl(this.returnUrl);
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
    this.submitWFContent(true);
  }

  cancelEdit() {
    if (this.isNewRec) {
      this.goBackHome();
    }
    this.editing = false;
    this.updateRecordLocalCopy(this.recordBackup);
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
