import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { Container } from '../../model/container.model';
import { ApiUrlService } from '../../core/api-url.service';

@Component({
  selector: 'ebx-open-url-button',
  templateUrl: './open-url-button.component.html',
  styleUrls: ['./open-url-button.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class OpenUrlButtonComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  buttonText = "Open URL";

  constructor(private route: ActivatedRoute, private apiUrlService: ApiUrlService) { }

  ngOnInit() {
  }

  openUrl() {

    if (this.record.__urls && this.record.__urls.length > 0) {
    
      let url = this.record.__urls[0].url;
      let navUrl = this.apiUrlService.getExtLinkUrl(url, this.record.identity, this.container.qualifiedId);

      window.open(navUrl, '_blank');

    }

  }

}
