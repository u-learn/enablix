import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { TPIntegrationMetadata } from '../../../model/integration.model';
import { ConfigInfoService } from '../../../services/config-info.service';
import { AlertService } from '../../../core/alert/alert.service';

@Component({
  selector: 'ebx-tpintegrations',
  templateUrl: './tpintegrations.component.html',
  styleUrls: ['./tpintegrations.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TPIntegrationsComponent implements OnInit {

  integrations: any[];

  constructor(private configInfoService: ConfigInfoService,
    private alert: AlertService) { }

  ngOnInit() {
    this.integrations = [];
    TPIntegrationMetadata.metadata.forEach(intgrMd => {
      
      let intg: any = {
        metadata: intgrMd,
        editing: false,
        instance: null,
        backupInstance: null
      };

      this.integrations.push(intg);

      this.configInfoService.getConfigByKey(intgrMd.tpKey).subscribe(res => {
          this.updateIntegrationInstance(intg, res);
        }, err => {
          this.alert.error("Error getting configuration for " + intgrMd.tpName, err.status);
        });
    });
  }

  updateIntegrationInstance(intg: any, intgInstance: any) {
    intg.instance = intgInstance;
    intg.backupInstance = intgInstance ? JSON.parse(JSON.stringify(intgInstance)) : null;
  }

  editTPIntegration(intg: any) {
    intg.editing = true;
    if (!intg.instance) {
      intg.instance = {
        key: intg.metadata.tpKey,
        config: {}
      };  
    }
  }

  cancelOperation(intg: any) {
    intg.editing = false;
    this.updateIntegrationInstance(intg, intg.backupInstance);
  }

  saveTPIntegration(intg: any) {

    this.configInfoService.saveConfiguration(intg.instance).subscribe(res => {
        intg.editing = false;
        this.updateIntegrationInstance(intg, res);
      }, err => {
        this.alert.error("Unable to save. Please try later.", err.status);
      });
  }

  deleteTPIntegration(intg: any) {
    if (intg && intg.instance && intg.instance.identity) {
      this.configInfoService.deleteByIdentity(intg.instance.identity).subscribe(res => {
        this.updateIntegrationInstance(intg, null);
      }, err => {
        this.alert.error("Unable to delete. Please try later.", err.status);
      })  
    }
    
  }

}
