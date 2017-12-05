import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { Container } from '../../model/container.model';
import { ContentTemplateService } from '../../core/content-template.service';

@Component({
  selector: 'ebx-card-preview',
  templateUrl: './card-preview.component.html',
  styleUrls: ['./card-preview.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CardPreviewComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  cardColor: string;
  text: string;
  thumbnailUrl: string;

  isBizDimension: boolean = false;

  constructor(private ctService: ContentTemplateService) { }

  ngOnInit() {

    this.cardColor = this.container.color ;
    this.isBizDimension = this.ctService.isBusinessDimension(this.container);

    if (this.record.__decoration) {
      let decoration = this.record.__decoration;
      this.thumbnailUrl = decoration.__thumbnailUrl;
      
      if (!this.thumbnailUrl) {
        this.text = decoration.__textContent;
      }
    }
  }

  getNoPreviewImage() : string {
    
    let imgSrc = "/assets/images/icons/file_unknown.svg";
    
    if (this.record.__decoration && this.record.__decoration.__noPreviewImgUrl) {
      imgSrc = this.record.__decoration.__noPreviewImgUrl;
    } 

    return imgSrc;
  }

}
