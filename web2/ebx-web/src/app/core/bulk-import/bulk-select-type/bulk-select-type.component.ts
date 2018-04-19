import { Component, OnInit, ViewEncapsulation, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

import { ContentTemplateService } from '../../content-template.service';
import { SelectOption } from '../../select/select.component';

@Component({
  selector: 'ebx-bulk-select-type',
  templateUrl: './bulk-select-type.component.html',
  styleUrls: ['./bulk-select-type.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class BulkSelectTypeComponent implements OnInit {

  selectedType: SelectOption[];
  contentTypes: SelectOption[];

  constructor(private contentTemplateService: ContentTemplateService,
    private dialogRef: MatDialogRef<BulkSelectTypeComponent>,
    @Inject(MAT_DIALOG_DATA) public data: any) { }

  ngOnInit() {
    this.contentTypes = this.contentTemplateService.getBizContentSelectOptions();
    this.contentTypes.sort((a, b) => (a.label > b.label ? 1 : -1));
  }

  contentTypeSelected() {
    this.dialogRef.close(this.selectedType);
  }

}
