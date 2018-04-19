import { Component, OnInit, ViewEncapsulation, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { ContentTemplateService } from '../../content-template.service';
import { ContentItem } from '../../../model/content-item.model';

@Component({
  selector: 'ebx-bulk-add-tags',
  templateUrl: './bulk-add-tags.component.html',
  styleUrls: ['./bulk-add-tags.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class BulkAddTagsComponent implements OnInit {

  tagItems: ContentItem[];
  tags: any = {};

  constructor(private contentTemplateService: ContentTemplateService,
    private dialogRef: MatDialogRef<BulkAddTagsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.tagItems = this.contentTemplateService.getBizDimBoundedItems();
  }

  tagsSelected() {
    this.dialogRef.close(this.tags);
  }

}
