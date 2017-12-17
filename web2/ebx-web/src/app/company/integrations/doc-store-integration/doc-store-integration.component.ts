import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormControl } from '@angular/forms';

import { AlertService } from '../../../core/alert/alert.service';
import { DocStoreConfigService } from './doc-store-config.service';
import { SelectOption } from '../../../core/select/select.component';
import { DocStoreMetadata } from '../../../model/integration.model';

@Component({
  selector: 'ebx-doc-store-integration',
  templateUrl: './doc-store-integration.component.html',
  styleUrls: ['./doc-store-integration.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DocStoreIntegrationComponent implements OnInit {

  docstore: any;
  docstoreBackup: any;
  docstoreTypeOptions: SelectOption[];
  dsConfigInfo: any;

  docstoreTypeCtrl: FormControl;
  selectDocstoreMd: any;

  editing: boolean = false;


  constructor(
      private alert: AlertService,
      private dcConfigService: DocStoreConfigService) { 
    this.docstoreTypeCtrl = new FormControl();
  }

  ngOnInit() {

    this.docstoreTypeOptions =
      DocStoreMetadata.metadata.map(dsMetadata => {
        return {
          id: dsMetadata.storeTypeCode,
          label: dsMetadata.storeTypeName  
        };
      });

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

  addDocstore() {
    this.editing = true;
    this.docstore = {};
  }

  editDocstore() {
    this.editing = true;
  }

  saveDocstoreConfig() {

    this.dcConfigService.saveDocstoreConfig(this.docstore, this.selectDocstoreMd.storeTypeCode).subscribe(res => {
        this.editing = false;
        this.updateAndBackupLocalCopy(res);
      }, err => {
        this.alert.error("Error updating document store configuration.", err.status);
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
