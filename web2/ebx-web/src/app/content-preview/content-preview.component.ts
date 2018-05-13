import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { ContentPreviewService } from '../core/content/content-preview.service';
import { ContentPreviewHandler } from '../core/content/content-preview-handler';
import { Container } from '../model/container.model';

@Component({
  selector: 'ebx-content-preview',
  templateUrl: './content-preview.component.html',
  styleUrls: ['./content-preview.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentPreviewComponent implements OnInit {

  _record: any;
  @Input() container: Container;
  @Input() editing: boolean = false;

  previewType: string;
  previewHandler: ContentPreviewHandler;

  get record() : any {
    return this._record;
  }

  @Input() 
  set record(rec : any) {
    this._record = rec;
    this.ngOnInit();
  }

  constructor(private contentPreviewService: ContentPreviewService) { }

  ngOnInit() {
    this.previewHandler = this.contentPreviewService.getPreviewHandler(this.record);
    if (this.previewHandler) {
      this.previewType = this.previewHandler.type();
    }
  }

  getDefaultFileImg() : string {
    let imgUrl = "/assets/images/icons/file_unknown.svg";
    if (this.previewHandler) {
      imgUrl = this.previewHandler.largeThumbnailUrl(this._record);
    }

    return imgUrl;
  }

  getPreviewStatusText() : string {
    return this._record.__decoration && this._record.__decoration.__docMetadata &&
            this._record.__decoration.__docMetadata.previewStatus == 'PENDING' ? 
            "Generating preview for this asset." : "Preview is not available for this asset.";
  }

}
