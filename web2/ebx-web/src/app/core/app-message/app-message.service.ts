import { Injectable, TemplateRef } from '@angular/core';
import { ComponentType } from '@angular/cdk/portal';
import { MatDialog, MatDialogConfig } from '@angular/material';

@Injectable()
export class AppMessageService {

  constructor(private dialog: MatDialog) { }

  show(componentOrTemplateRef: ComponentType<any> | TemplateRef<any>, config?: MatDialogConfig) {
    
    let mtCfg: MatDialogConfig = config ? config : {
      width: '624px',
      hasBackdrop: false
    };

    this.dialog.open(componentOrTemplateRef, mtCfg);
  }

}
