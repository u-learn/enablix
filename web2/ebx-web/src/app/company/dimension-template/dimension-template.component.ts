import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';

import { ContentTemplateService } from '../../core/content-template.service';
import { AlertService } from '../../core/alert/alert.service';
import { ContentTemplate } from '../../model/content-template.model';
import { ConfirmDialogComponent } from '../../core/confirm-dialog/confirm-dialog.component'; 

@Component({
  selector: 'ebx-dimension-template',
  templateUrl: './dimension-template.component.html',
  styleUrls: ['./dimension-template.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DimensionTemplateComponent implements OnInit {

  dimContainers: any[];

  constructor(private ctService: ContentTemplateService,
    private alert: AlertService, private dialog: MatDialog) { }

  ngOnInit() {
    this.dimContainers = [];
    this.ctService.templateCache.bizDimensionContainers.forEach(
      (dimCont) => {
        this.dimContainers.push({
          editing: false,
          instance: dimCont,
          instanceCopy: JSON.parse(JSON.stringify(dimCont))  
        });
      }
    );
  }

  editDimension(dim: any, indx: number) {
    dim.editing = true;
  }

  cancelEditing(dim: any, indx: number) {
    
    if (!dim.instance.qualifiedId) {
      // cancellation of add, remove it
      this.dimContainers.splice(indx, 1);

    } else {
      dim.instanceCopy = JSON.parse(JSON.stringify(dim.instance));
      dim.editing = false;
    }
  }

  onSave(dim: any, indx: number, form: any) {
    
    if (form.invalid) {
      Object.keys(form.controls).forEach(key => {
        form.controls[key].markAsDirty();
      });
      return;
    }

    if (dim.instance.qualifiedId) {
      this.updateContainer(dim, indx);
    } else {
      this.submitAddDimContainer(dim, indx);
    }
  }

  updateContainer(dim: any, indx: number) {
    
    this.ctService.updateContainerDefinition(dim.instanceCopy).subscribe(
      (result: ContentTemplate) => {
        if (result) {
          dim.instance = Object.assign(dim.instance, dim.instanceCopy);
          this.ctService.loadTemplate(result);
          this.cancelEditing(dim, indx);
        }
      },
      (err) => {
        this.alert.error("Error updating dimension. Please try later.", err.status);
      }
    );
  }

  addNewContainer() {
    
    this.dimContainers.push({
      editing: true,
      instance: {},
      instanceCopy: {}
    });

    setTimeout(function(){
      window.scrollTo({left: 0, top: document.body.scrollHeight, behavior: 'smooth'});
    }, 100);
    
  }

  submitAddDimContainer(dim: any, indx: number) {
    
    dim.instanceCopy.referenceable = true;
    dim.instanceCopy.businessCategory = 'BUSINESS_DIMENSION';

    this.ctService.addContainerDefinition(dim.instanceCopy).subscribe(
      (result: any) => {
        if (result) {
          dim.instance = result.container;
          this.ctService.loadTemplate(result.template);
          this.cancelEditing(dim, indx);
        }
      },
      (err) => {
        this.alert.error("Error adding new dimension. Please try later.", err.status);
      }
    );
  }

  deleteAction(dim: any, indx: number) {

    let deleteMsg = "You are about to delete '" + dim.instance.label + "' dimension and corresponding objects."
        + " You won't be able to restore it. Would you like to proceed?";

    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
        width: '624px',
        disableClose: true,
        data: { 
          title: "Delete Dimension",
          text: deleteMsg,
          confirmLabel: "Delete",
          cancelLabel: "No, keep it."
        }
      });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.deleteDimension(dim, indx);
      }
    });
  }

  deleteDimension(dim: any, indx: number) {

    this.ctService.deleteContainerDefinition(dim.instance.qualifiedId).subscribe(
      (result: any) => {
        if (result) {
          this.ctService.loadTemplate(result.template);
          this.dimContainers.splice(indx, 1);
        }
      },
      (err) => {
        this.alert.error("Error deleting dimension. Please try later.", err.status);
      }
    );
  }

}
