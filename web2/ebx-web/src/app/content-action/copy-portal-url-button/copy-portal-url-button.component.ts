import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { Location } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

import { Container } from '../../model/container.model';
import { ContentService } from '../../core/content/content.service';
import { AlertService } from '../../core/alert/alert.service';
import { Utility } from '../../util/utility';

@Component({
  selector: 'ebx-copy-portal-url-button',
  templateUrl: './copy-portal-url-button.component.html',
  styleUrls: ['./copy-portal-url-button.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CopyPortalUrlButtonComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  buttonText = "Copy Asset URL";

  constructor(private route: ActivatedRoute, private location: Location) { }

  ngOnInit() {
  }

  copy() {
    // http://localhost:4200/portal/content/detail/faq/5ff46989-b6a7-458c-a181-fe2ae55d84ab
    let url = window.location.protocol + "//" + window.location.host + "/portal/content/detail/"
      + this.container.qualifiedId + "/" + this.record.identity;

    Utility.copyToClipboard(url);
    
    this.buttonText = "Copied to Clipboard";
    
    setTimeout(() => {
      this.buttonText = "Copy Asset URL";
    }, 4000);
  }

}
