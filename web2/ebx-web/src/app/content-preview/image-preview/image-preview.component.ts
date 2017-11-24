import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { ImagePreviewHandler } from '../../core/content/image-preview-handler';

@Component({
  selector: 'ebx-image-preview',
  templateUrl: './image-preview.component.html',
  styleUrls: ['./image-preview.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ImagePreviewComponent implements OnInit {

  @Input() record: any;

  imagePreviewHandler: ImagePreviewHandler;
  imageUrl: string;

  constructor() { 
    this.imagePreviewHandler = new ImagePreviewHandler();
  }

  ngOnInit() {
    if (this.record) {
      this.imageUrl = this.imagePreviewHandler.getImageUrl(this.record);  
    }
    
  }

}
