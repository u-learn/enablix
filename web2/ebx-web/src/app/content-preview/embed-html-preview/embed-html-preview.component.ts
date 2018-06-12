import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { Container } from '../../model/container.model';

@Component({
  selector: 'ebx-embed-html-preview',
  templateUrl: './embed-html-preview.component.html',
  styleUrls: ['./embed-html-preview.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class EmbedHtmlPreviewComponent implements OnInit {

  @Input() record: any;
  @Input() container: Container;

  embedHtml: string;

  constructor() { }

  ngOnInit() {
  }

}
