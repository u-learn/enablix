import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { ImagePreviewHandler } from '../../core/content/image-preview-handler';

@Component({
  selector: 'ebx-image-preview',
  templateUrl: './image-preview.component.html',
  styleUrls: ['./image-preview.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ImagePreviewComponent implements OnInit {

  _record: any;

  imagePreviewHandler: ImagePreviewHandler;
  imageUrl: string;

  @Input() set record(rec: any) {
    this._record = rec;
    this.updateImgUrl();
  }

  get record() : any {
    return this._record;
  }
  
  constructor() { 
    this.imagePreviewHandler = new ImagePreviewHandler();
  }

  ngOnInit() {
    
  }

  updateImgUrl() {
    if (this._record) {
      this.imageUrl = this.imagePreviewHandler.getImageUrl(this.record);  
    }
  }

}
