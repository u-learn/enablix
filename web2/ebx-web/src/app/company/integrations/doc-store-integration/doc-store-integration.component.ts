import { Component, OnInit, ViewEncapsulation, ViewChild } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material';

import { AlertService } from '../../../core/alert/alert.service';
import { DocStoreConfigService } from './doc-store-config.service';
import { SelectOption } from '../../../core/select/select.component';
import { DocStoreMetadata } from '../../../model/integration.model';
import { ConfirmDialogComponent } from '../../../core/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'ebx-doc-store-integration',
  templateUrl: './doc-store-integration.component.html',
  styleUrls: ['./doc-store-integration.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DocStoreIntegrationComponent implements OnInit {

  @ViewChild('docstoreForm') form;

  docstore: any;
  docstoreBackup: any;
  docstoreTypeOptions: SelectOption[];
  dsConfigInfo: any;

  docstoreTypeCtrl: FormControl;
  selectDocstoreMd: any;

  editing: boolean = false;

  enablixDSMetadata: any;

  constructor(
      private dialog: MatDialog,
      private alert: AlertService,
      private dcConfigService: DocStoreConfigService) { 
    this.docstoreTypeCtrl = new FormControl();
  }

  printForm(form) {
    console.log(form);
  }

  ngOnInit() {

    this.docstoreTypeOptions =
      DocStoreMetadata.metadata.map(dsMetadata => {
        return {
          id: dsMetadata.storeTypeCode,
          label: dsMetadata.storeTypeName  
        };
      });

    this.enablixDSMetadata = this.getEnablixDocstore();

    this.docstoreTypeCtrl.valueChanges.subscribe(vals => {
        
        let val = vals && vals.length > 0 ? vals[0] : null;
        
        if (val) {
          if (!this.selectDocstoreMd || this.selectDocstoreMd.storeTypeCode != val.id) {
            this.docstore = {};  
          }
          this.selectDocstoreMd = this.getDocstoreMetadata(val.id);
          this.docstore.STORE_TYPE = val.id;
        }
        
      });

    this.dcConfigService.getDefaultDocstoreConfig().subscribe((res: any) => {
        if (res) {
          this.dsConfigInfo = res;
          this.updateAndBackupLocalCopy(res);
        }
      }, err => {
        this.alert.error("Error getting document store configurations.", err.status);
      });

  }

  getDocstoreMetadata(storeType: string) {
    let docMdList = DocStoreMetadata.metadata;
        
    for (let i = 0; i < docMdList.length; i++) {
      if (docMdList[i].storeTypeCode == storeType) {
        this.selectDocstoreMd = docMdList[i]
        return docMdList[i];
      }
    }

    return null;
  }

  getDocstoreName(docstore:any) {
    let docMdList = DocStoreMetadata.metadata;
    for (let i = 0; i < docMdList.length; i++) {
      if (docMdList[i].storeTypeCode == docstore.STORE_TYPE) {
        return docMdList[i].storeTypeName;
      }
    }
    return "";
  }

  getDocstoreDesc(docstore:any) {
    let docMdList = DocStoreMetadata.metadata;
    for (let i = 0; i < docMdList.length; i++) {
      if (docMdList[i].storeTypeCode == docstore.STORE_TYPE) {
        var dsMd = docMdList[i];
        return dsMd.storeDesc ? dsMd.storeDesc : "";
      }
    }
    return "";
  }

  addDocstore() {
    this.editing = true;
    this.docstore = {};
  }

  editDocstore() {
    this.editing = true;
  }

  isDeletable(docstore: any) {
    return docstore && docstore.STORE_TYPE != this.enablixDSMetadata.storeTypeCode;
  }

  deleteDocstore() {
    
    let dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '624px',
      disableClose: true,
      data: { 
        title: 'Delete File Storage',
        text: 'You are about to Delete file storage configuration. You cannot undo this operation. Would you like to proceed?',
        confirmLabel: 'Proceed',
        cancelLabel: 'Cancel'
      }
    });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.doDeleteDocstore();
      }
    });

    
  }

  doDeleteDocstore() {

    var docstoreConfig = {};
    docstoreConfig['STORE_TYPE'] = this.enablixDSMetadata.storeTypeCode;

    this.dcConfigService.saveDocstoreConfig(docstoreConfig, this.enablixDSMetadata.storeTypeCode).subscribe(res => {
        this.updateAndBackupLocalCopy(res);
      }, err => {
        this.alert.error("Error deleting file storage configuration.", err.status);
      });
  }

  getEnablixDocstore() {
    let docMdList = DocStoreMetadata.metadata;
    for (let i = 0; i < docMdList.length; i++) {
      if (docMdList[i].storeTypeCode == 'DISK') {
        return docMdList[i];
      }
    }
    return null;
  }

  saveDocstoreConfig() {

    if (this.form.invalid) {
      Object.keys(this.form.controls).forEach(key => {
        this.form.controls[key].markAsDirty();
      });
      return;
    }

    this.dcConfigService.saveDocstoreConfig(this.docstore, this.selectDocstoreMd.storeTypeCode).subscribe(res => {
        this.editing = false;
        this.updateAndBackupLocalCopy(res);
      }, err => {
        this.alert.error("Error updating file storage configuration.", err.status);
      });
  }

  cancelOperation() {
    this.editing = false;
    this.updateDocstoreCopy(this.docstoreBackup);
  }

  updateAndBackupLocalCopy(dsConfigInfo: any) {
    this.dsConfigInfo = dsConfigInfo;
    this.updateDocstoreCopy(dsConfigInfo.config);
  }

  updateDocstoreCopy(ds: any) {

    this.selectDocstoreMd = ds ? this.getDocstoreMetadata(ds.STORE_TYPE) : null;

    this.docstore = ds;
    this.docstoreBackup = this.docstore ? JSON.parse(JSON.stringify(this.docstore)) : this.docstore;   

    if (this.selectDocstoreMd) {
      
      this.docstoreTypeCtrl.setValue([{
            id: this.selectDocstoreMd.storeTypeCode, 
            label: this.selectDocstoreMd.storeTypeName
          }]); 

    } else {
      this.docstoreTypeCtrl.setValue([]);
    }
  }

}
