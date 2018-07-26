import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { MatDialog, MatDialogRef } from '@angular/material';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs/Observable';

import { ContentTemplateService } from '../../core/content-template.service';
import { AlertService } from '../../core/alert/alert.service';
import { ContentTemplate } from '../../model/content-template.model';
import { ConfirmDialogComponent } from '../../core/confirm-dialog/confirm-dialog.component'; 
import { MessageDialogComponent } from '../../core/message-dialog/message-dialog.component'; 
import { SelectOption } from '../../core/select/select.component';

@Component({
  selector: 'ebx-container-template',
  templateUrl: './container-template.component.html',
  styleUrls: ['./container-template.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContainerTemplateComponent implements OnInit {

  verifyInDaysOpts: SelectOption[];

  containerList: any[];
  filteredContainers: Observable<any>;

  cntnrType: string;
  cntnrTypeLbl: string;

  cntnrTypeBizDim: boolean;
  cntnrTypeBizContent: boolean;

  bizCategory: string;

  nameCtrl: FormControl;

  constructor(private route: ActivatedRoute,
    private ctService: ContentTemplateService,
    private alert: AlertService, private dialog: MatDialog) { }

  isBizDimension() {
    return this.bizCategory == 'dim';
  }

  isBizContent() {
    return this.bizCategory == 'cntnt';
  }

  ngOnInit() {
    
    this.bizCategory = this.route.snapshot.data['bizCategory'] || 'dim';
    this.verifyInDaysOpts = [
      {
        id: '30',
        label: '30 Days'
      },
      {
        id: '90',
        label: '90 Days'
      },
      {
        id: '180',
        label: '180 Days'
      },
      {
        id: '365',
        label: '1 year'
      }
    ];
    
    this.containerList = [];
    var targetContainers = null;

    if (this.isBizContent()) {
      this.cntnrType = "content";
      this.cntnrTypeLbl = "Contents";
      this.cntnrTypeBizDim = false;
      this.cntnrTypeBizContent = true;
      this.initContainerList(this.ctService.templateCache.bizContentContainers);

      this.indexContainerList();

      this.nameCtrl = new FormControl();
      this.filteredContainers = this.nameCtrl.valueChanges.startWith(null)
        .map(ct => ct ? this.filterContainers(ct) : this.containerList);

    } else if (this.isBizDimension()) {
      this.cntnrType = "dimension";
      this.cntnrTypeLbl = "Dimensions";
      this.cntnrTypeBizDim = true;
      this.cntnrTypeBizContent = false;
      this.initContainerList(this.ctService.templateCache.bizDimensionContainers);

      this.filteredContainers = Observable.of(this.containerList);
    }
    
  }

  indexContainerList() {
    
    if (this.cntnrTypeBizContent) {
      this.containerList.sort((a: any, b: any) => {
        return a.instance.label == b.instance.label ? 0 : 
                  (a.instance.label > b.instance.label ? 1 : -1); 
      });
    }

    var indx = 0;
    this.containerList.forEach(cont => cont.index = indx++);
  }

  initContainerList(targetContainers: any[]) {

    if (targetContainers) {
      
      var indx = 0;
      
      targetContainers.forEach(
        (cont) => {
          
          let cntnrItem: any = {
            editing: false,
            index: indx++,
            instance: cont,
            instanceCopy: JSON.parse(JSON.stringify(cont))
          };

          this.addVerifyInDaysControl(cntnrItem);
          this.containerList.push(cntnrItem);
        }
      );
    }
  }

  getObsoleteInDaysParam(contInstance: any) {

    if (contInstance.qualityConfig && contInstance.qualityConfig.param) {

      for (let param of contInstance.qualityConfig.param) {
        if (param.name == 'OBSOLETE_IN_DAYS') {
          return param;
        }
      }
    }

    return null;
  }

  hasVerifyInDaysConfig(contInstance: any) {
    return this.getObsoleteInDaysParam(contInstance) != null;
  }

  getVerifyInDaysLabel(contInstance: any) {
    
    let param = this.getObsoleteInDaysParam(contInstance);
    
    if (param) {
      for (let opt of this.verifyInDaysOpts) {
        if (opt.id == param.value) {
          return opt.label;
        }
      }
      // if not found in opts
      return param.value + " Days";
    }

    return "";
  }

  addVerifyInDaysControl(cntnrItem: any) {

    if (this.isBizContent) {

      cntnrItem.verifyInDaysCtrl = new FormControl();
      
      let initVal = [this.verifyInDaysOpts[0]];
      
      if (cntnrItem.instanceCopy.qualityConfig) {
      
        for (let param of cntnrItem.instanceCopy.qualityConfig.param) {
      
          if (param.name == 'OBSOLETE_IN_DAYS') {
      
            for (let opt of this.verifyInDaysOpts) {
              if (opt.id == param.value) {
                initVal = [opt];
                break;
              }
            }
      
            break;
          }
        }
      }

      cntnrItem.verifyInDaysCtrl.valueChanges.subscribe(
        val => {
          if (val && val[0]) {
            
            var verifyInParam = {
              'name': 'OBSOLETE_IN_DAYS',
              'value': val[0].id
            };

            if (cntnrItem.instanceCopy.qualityConfig) {

              if (cntnrItem.instanceCopy.qualityConfig.param) {
              
                var updated = false;
              
                for (let param of cntnrItem.instanceCopy.qualityConfig.param) {
                  if (param.name == verifyInParam.name) {
                    param.value = verifyInParam.value;
                    updated = true;
                    break;
                  }
                }

                if (!updated) {
                  cntnrItem.instanceCopy.qualityConfig.param.push(verifyInParam);
                }

              } else {
                cntnrItem.instanceCopy.qualityConfig.param = [verifyInParam];
              }

            } else {
              cntnrItem.instanceCopy.qualityConfig = {
                param: [verifyInParam]
              }
            }
          }
        }
      );
    }
  }

  filterContainers(ct: string) {
    return this.containerList.filter(cont =>
      (cont.editing) || 
        (cont.instance.label && cont.instance.label.toLowerCase().indexOf(ct.toLowerCase()) >= 0)
        || (cont.instance.singularLabel && cont.instance.singularLabel.toLowerCase().indexOf(ct.toLowerCase()) >= 0));
  }

  editContainer(cont: any, indx: number) {
    cont.editing = true;
  }

  cancelEditing(cont: any) {
    
    if (!cont.instance.qualifiedId) {
      // cancellation of add, remove it
      this.containerList.splice(cont.index, 1);
      this.indexContainerList();
      this.refreshFilteredlist();

    } else {
      cont.instanceCopy = JSON.parse(JSON.stringify(cont.instance));
      cont.editing = false;
    }
    
  }

  onSave(cont: any, form: any) {
    
    if (form.invalid) {
      Object.keys(form.controls).forEach(key => {
        form.controls[key].markAsDirty();
      });
      return;
    }

    if (cont.instance.qualifiedId) {
      this.updateContainer(cont);
    } else {
      this.submitAddContainer(cont);
    }
  }

  updateContainer(cont: any) {
    
    this.ctService.updateContainerDefinition(cont.instanceCopy).subscribe(
      (result: ContentTemplate) => {
        if (result) {
          cont.instance = Object.assign(cont.instance, cont.instanceCopy);
          this.ctService.loadTemplate(result);
          this.cancelEditing(cont);
        }
      },
      (err) => {
        var msg = "Error updating " + this.cntnrType + ". Please try later.";
        this.alert.error(msg, err.status);
      }
    );
  }

  addNewContainer() {
    
    let cntnrItem = {
      editing: true,
      index: this.containerList.length,
      instance: {},
      instanceCopy: {}
    };

    this.addVerifyInDaysControl(cntnrItem);
    this.containerList.push(cntnrItem);

    this.refreshFilteredlist();

    setTimeout(function(){
      window.scrollTo({left: 0, top: document.body.scrollHeight, behavior: 'smooth'});
    }, 100);
    
  }

  refreshFilteredlist() {
    if (this.nameCtrl) {
      this.nameCtrl.setValue(this.nameCtrl.value);
    }
  }

  submitAddContainer(cont: any) {
    
    cont.instanceCopy.referenceable = true;

    if (this.cntnrTypeBizContent) {
      cont.instanceCopy.businessCategory = 'BUSINESS_CONTENT';
    } else if (this.cntnrTypeBizDim) {
      cont.instanceCopy.businessCategory = 'BUSINESS_DIMENSION';  
    }

    this.ctService.addContainerDefinition(cont.instanceCopy).subscribe(
      (result: any) => {
        if (result) {
          cont.instance = result.container;
          this.ctService.loadTemplate(result.template);
          this.cancelEditing(cont);
          this.indexContainerList();
          this.refreshFilteredlist();
        }
      },
      (err) => {
        this.alert.error("Error adding new " + this.cntnrType + ". Please try later.", err.status);
      }
    );
  }

  deleteAction(cont: any) {

    let deleteMsg = null;
    let deleteLbl = null;
    if (this.cntnrTypeBizDim) {
      
      deleteMsg = "You are about to delete '" + cont.instance.label + "' dimension and corresponding objects."
        + " You won't be able to restore it. Would you like to proceed?";
      deleteLbl = "Delete Dimension";

    } else if (this.cntnrTypeBizContent) {
      deleteMsg = "You are about to delete '" + cont.instance.label + "' content type."
        + " You won't be able to restore it. Would you like to proceed?";
      deleteLbl = "Delete Content Type";
    }

    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
        width: '624px',
        disableClose: true,
        data: { 
          title: deleteLbl,
          text: deleteMsg,
          confirmLabel: "Delete",
          cancelLabel: "No, keep it."
        }
      });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.deleteContainer(cont);
      }
    });
  }

  deleteContainer(cont: any) {

    this.ctService.deleteContainerDefinition(cont.instance.qualifiedId).subscribe(
      (result: any) => {
        if (result) {
          this.ctService.loadTemplate(result.template);
          this.containerList.splice(cont.index, 1);
          this.indexContainerList();
          this.refreshFilteredlist();
        }
      },
      (err) => {
        
        if (err.error && err.error.error && err.error.error.errorCode == "validation_error") {
          
          var msg = "";
          err.error.errors.forEach((e) => msg += (e.errorMessage + "<br>"));
          
          this.dialog.open(MessageDialogComponent, {
            width: '624px',
            disableClose: true,
            data: { 
              title: "Errors",
              text: msg,
              okLabel: "OK",
              type: "ERROR"
            }
          });

        } else {
          this.alert.error("Error deleting " + this.cntnrType + ". Please try later.", err.status);
        }

      }
    );
  }

  moveContUp(dim: any) {
    this.swapContainer(dim, dim.index, dim.index - 1);
  }

  moveContDown(dim: any) {
    this.swapContainer(dim, dim.index, dim.index + 1);
  }

  swapContainer(cont: any, indx: number, swapWithIndx: number) {
    
    if (swapWithIndx >= 0 && swapWithIndx < this.containerList.length) {
      
      let swapWith = this.containerList[swapWithIndx];
      
      var order = {
        containerOrder: {}
      };
      
      order.containerOrder[cont.instance.qualifiedId] = swapWith.instance.displayOrder;
      order.containerOrder[swapWith.instance.qualifiedId] = cont.instance.displayOrder;

      this.ctService.updateContainerOrder(order).subscribe(
        (result: any) => {
          if (result) {
            
            this.ctService.loadTemplate(result);
            
            cont.instance.displayOrder = order.containerOrder[swapWith.instance.qualifiedId];
            cont.instanceCopy.displayOrder = cont.instance.displayOrder;
            
            swapWith.instance.displayOrder = order.containerOrder[cont.instance.qualifiedId];
            swapWith.instanceCopy.displayOrder = order.containerOrder[cont.instance.qualifiedId];

            this.containerList[indx] = swapWith;
            this.containerList[swapWithIndx] = cont;

            this.indexContainerList();
          }
        },
        (err) => {
          this.alert.error("Error moving " + this.cntnrType + ". Please try later.", err.status);
        }
      );
    }
  }

}
