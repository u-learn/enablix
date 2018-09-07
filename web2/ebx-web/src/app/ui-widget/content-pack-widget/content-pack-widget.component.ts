import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { ContentBrowserComponent } from '../../content-browser/content-browser.component';
import { UiWidgetService } from '../../services/ui-widget.service';
import { ContentTemplateService } from '../../core/content-template.service';
import { AlertService } from '../../core/alert/alert.service';
import { UserService } from '../../core/auth/user.service';
import { Permissions } from '../../model/permissions.model';

@Component({
  selector: 'ebx-content-pack-widget',
  templateUrl: './content-pack-widget.component.html',
  styleUrls: ['./content-pack-widget.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentPackWidgetComponent implements OnInit {

  @Input() details: any;

  showManageAssets: boolean;

  constructor(private dialog: MatDialog, private uiWdgtService: UiWidgetService,
    private alert: AlertService, private ctService: ContentTemplateService,
    private user: UserService) { }

  ngOnInit() {
    this.showManageAssets = this.details.data.pack 
        && this.details.data.pack.type == 'SELECTED_CONTENT'
        && this.user.userHasPermission(Permissions.MANAGE_DASHBOARD);
  }

  openContentPicker() {
    this.uiWdgtService.getWidgetData(this.details.definition.identity, 0, 100).subscribe(
      res => {
        this.showContentPicker(res);
      },
      err => {
        this.alert.error("Error fetching " + this.details.definition.title 
          + " data", err.statusCode);
      }
    );

  }

  showContentPicker(widgetDetails: any) {

    let dialogRef = this.dialog.open(ContentBrowserComponent, {
        width: '80vw',
        height: '90vh',
        disableClose: true,
        autoFocus: false,
        data: { 
          selectedItems: this.convertContentPackToBrowserItems(widgetDetails.data),
          groupByQId: false,
          sortable: true
        }
      });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.persistWidgetSelectedItems(widgetDetails, result);
      }
    });
  }

  persistWidgetSelectedItems(widgetDetails: any, selItems: any) {
    
    let contentPointers = [];

    if (selItems) {
    
      let templateId = this.ctService.contentTemplate.id;
      let indx = 0;
    
      selItems.forEach((item) => {
        
        contentPointers.push({
          order: ++indx,
          data: {
            type: 'CONTENT',
            templateId: templateId,
            containerQId: item.qualifiedId,
            title: item.label,
            instanceIdentity: item.identity
          }
        });

      });
    }

    let selContentPack = {
      pack: widgetDetails.data.pack,
      records: contentPointers
    } 

    this.uiWdgtService.updateSelectedContentPack(selContentPack).subscribe(
        result => {

          this.uiWdgtService.getWidgetData(this.details.definition.identity).subscribe(
            res => {
              this.details = res;
            },
            err => {
              this.alert.error("Error fetching widget data", err.statusCode);
            }
          );

          this.alert.success(widgetDetails.definition.title + " updated successfully.");
        },
        err => {
          this.alert.error("Unable to update " + widgetDetails.definition.title 
            + ". Please try later.", err.statusCode);
        }
      );

  }

  convertContentPackToBrowserItems(widgetData: any) {
    
    let browserItems = [];
    
    if (widgetData && widgetData.records && widgetData.records.content) {
    
      widgetData.records.content.forEach((item) => {
    
        let container = this.ctService.getConcreteContainerByQId(item.containerQId);
    
        if (container) {
    
          browserItems.push({
            identity: item.record.identity,
            label: item.record.__title,
            qualifiedId: container.qualifiedId,
            containerLabel: container.label
          });
        }
      });
    }

    return browserItems;
  }

}
