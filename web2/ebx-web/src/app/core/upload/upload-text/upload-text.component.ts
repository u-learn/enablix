import { Component, OnInit, ViewEncapsulation, Inject } from '@angular/core';
import { MatDialogRef } from '@angular/material';
import { FormControl } from '@angular/forms';

import { TextInfo } from '../../../model/upload-info.model';
import { SelectOption } from '../../select/select.component';
import { ContentTemplateService } from '../../content-template.service';
import { NewContentService } from '../../content/new-content.service';
import { NavigationService } from '../../../app-routing/navigation.service';

@Component({
  selector: 'ebx-upload-text',
  templateUrl: './upload-text.component.html',
  styleUrls: ['./upload-text.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class UploadTextComponent implements OnInit {

  contentTypeCtrl: FormControl;
  contentTypes: SelectOption[];

  textInfo: TextInfo;

  errors: any = {};

  constructor(private dialogRef: MatDialogRef<UploadTextComponent>,
              private contentTemplateService: ContentTemplateService,
              private navService: NavigationService,
              private newContentService: NewContentService) { }

  ngOnInit() {

    this.textInfo = new TextInfo();

    this.contentTypeCtrl = new FormControl();
      this.contentTypeCtrl.valueChanges.subscribe(
          val => {
            if (val && val[0]) {
              this.textInfo.containerQId = val[0].id;
              this.errors.contentType = null;
            }
          }
        );

      this.contentTypes = this.contentTemplateService.getBizContentSelectOptions();
      this.contentTypes.sort((a, b) => (a.label > b.label ? 1 : -1));
      this.newContentService.clear();
  }

  onTitleChange() {
    if (this.textInfo.title.trim().length > 0) {
      this.errors.title = null;
    }
  }

  createText() {
    let hasErrors = false;
    if (!this.textInfo.title || this.textInfo.title.trim().length == 0) {
      this.errors.title = { required: true };
      hasErrors = true;
    }

    if (!this.textInfo.containerQId) {
      this.errors.contentType = { required: true };
      hasErrors = true;
    }

    if (hasErrors) {
      return;
    }

    this.newContentService.captureTextContent(this.textInfo);
    this.navService.goToNewContentEdit(this.textInfo.containerQId);
    this.dialogRef.close();
  }
}
