import { Component, OnInit, ViewEncapsulation, Input, ViewChildren, AfterViewInit, QueryList } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { MatDialog, MatDialogRef } from '@angular/material';

import { Container } from '../model/container.model';
import { ContentItem } from '../model/content-item.model';
import { ContentService } from '../core/content/content.service';
import { ContentTemplateService } from '../core/content-template.service'; 
import { AlertService } from '../core/alert/alert.service';
import { NavigationService } from '../app-routing/navigation.service';
import { ConfirmDialogComponent } from '../confirm-dialog/confirm-dialog.component'; 
import { ContentDeleteButtonComponent } from '../content-action/content-delete-button/content-delete-button.component';

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
  @Input() editable?: boolean = false;

  private sub: any;

  private qualityAlerts: any[];

  inputItems: ContentItem[];
  boundedItems: ContentItem[];
  title: string;

  addnActions: RecordAction[] = [];

  constructor(private route: ActivatedRoute, private contentService: ContentService,
              private alert: AlertService, private contentTemplate: ContentTemplateService,
              private navService: NavigationService, private dialog: MatDialog) { }

  ngOnInit() {

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
                        this.alert.error("Error fetching record details");
                      }
                    );
            
          }

      });

    } else {
      this.contentService.decorateRecord(this.container, this.record);
      this.initState();
    }

  }

  addRecordActions() {

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
  }

  publishContent() {

    this.contentService.saveContainerData(this.container.qualifiedId, this.record)
        .subscribe(
            result => {
              
              this.editable = false;
              this.contentService.decorateRecord(
                this.container, result.contentRecord);
              this.record = result.contentRecord;

              this.alert.success("Saved successfully!");
              this.navService.goToContentDetail(
                this.container.qualifiedId, result.contentRecord.identity);
            },
            error => {
              if (error.qualityAlerts) {
                this.qualityAlerts = error.qualityAlerts;
              } else {
                this.alert.error("Error saving data.");  
              }
            }
          );
  }

  goBackHome() {
    this.navService.goToPortalHome();
  }

  editState() {
    this.editable = true;
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
      this.alert.error("Unable to delete. Please try later.");
    });
  }

  saveDraft() {

  }

  cancelEdit() {
    this.editable = false;
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
