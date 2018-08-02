import { Component, OnInit, ViewEncapsulation, Inject } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { FormControl } from '@angular/forms';

import { AssetInfo } from '../../../model/upload-info.model';
import { SelectOption } from '../../select/select.component';
import { ContentTemplateService } from '../../content-template.service';
import { NewContentService } from '../../content/new-content.service';
import { NavigationService } from '../../../app-routing/navigation.service';

@Component({
  selector: 'ebx-upload-asset',
  templateUrl: './upload-asset.component.html',
  styleUrls: ['./upload-asset.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UploadAssetComponent implements OnInit {

  contentTypeCtrl: FormControl;
  contentTypes: SelectOption[];

  assetInfo: AssetInfo;

  errors: any = {};

  constructor(private dialogRef: MatDialogRef<UploadAssetComponent>,
              private contentTemplateService: ContentTemplateService,
              private navService: NavigationService,
              private newContentService: NewContentService) { }

  ngOnInit() {

    this.assetInfo = new AssetInfo();

    this.contentTypeCtrl = new FormControl();
      this.contentTypeCtrl.valueChanges.subscribe(
          val => {
            if (val && val[0]) {
              this.assetInfo.containerQId = val[0].id;
              this.errors.contentType = null;
            }
          }
        );

      this.contentTypes = this.contentTemplateService.getBizContentSelectOptions();
      this.contentTypes.sort((a, b) => (a.label > b.label ? 1 : -1));
      this.newContentService.clear();
  }

  onTitleChange() {
    if (this.assetInfo.title.trim().length > 0) {
      this.errors.title = null;
    }
  }

  createAsset() {
    let hasErrors = false;
    if (!this.assetInfo.title || this.assetInfo.title.trim().length == 0) {
      this.errors.title = { required: true };
      hasErrors = true;
    }

    if (!this.assetInfo.containerQId) {
      this.errors.contentType = { required: true };
      hasErrors = true;
    }

    if (hasErrors) {
      return;
    }

    this.newContentService.captureAsset(this.assetInfo);
    this.navService.goToNewContentEdit(this.assetInfo.containerQId);
    this.dialogRef.close();
  }

}
