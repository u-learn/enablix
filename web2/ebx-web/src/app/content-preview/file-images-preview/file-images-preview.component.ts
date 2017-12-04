import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { environment } from '../../../environments/environment';

import { ContentPreviewService } from '../../core/content/content-preview.service';
import { AlertService } from '../../core/alert/alert.service';

@Component({
  selector: 'ebx-file-images-preview',
  templateUrl: './file-images-preview.component.html',
  styleUrls: ['./file-images-preview.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class FileImagesPreviewComponent implements OnInit {

  @Input() record: any;

  activeSlide: Slide;
  slides: Slide[] = [];

  constructor(private contentPreviewService: ContentPreviewService,
              private alert: AlertService) { 
    this.activeSlide = new Slide();
  }

  ngOnInit() {

    if (this.record.__decoration && this.record.__decoration.__docMetadata 
          && this.record.__decoration.__docMetadata.identity) {
      
      const docMdIdentity = this.record.__decoration.__docMetadata.identity;
      
      this.contentPreviewService.getFilePreviewData(docMdIdentity)
          .subscribe(
              res => {
                this.slides = [];
                for (let i = 0; i < res.parts.length; i++) {
                  this.slides.push({
                    id: i,
                    url: environment.baseAPIUrl + "/doc/pdp/" + docMdIdentity + "/" + i + "/"
                  })
                }

                this.activeSlide = this.slides[0];
              },
              err => {
                this.alert.error("Error fetching preview data", err.status);
              }
            );
    }

  }

  selectSlide(indx: number) {
    this.activeSlide = this.slides[indx];
  }

}

class Slide {
  id: number;
  url: string;
}