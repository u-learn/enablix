import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

@Component({
  selector: 'ebx-content-pack-widget',
  templateUrl: './content-pack-widget.component.html',
  styleUrls: ['./content-pack-widget.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentPackWidgetComponent implements OnInit {

  @Input() details: any;

  constructor() { }

  ngOnInit() {
  }

}
