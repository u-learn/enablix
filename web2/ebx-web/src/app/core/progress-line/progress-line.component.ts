import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

@Component({
  selector: 'ebx-progress-line',
  templateUrl: './progress-line.component.html',
  styleUrls: ['./progress-line.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ProgressLineComponent implements OnInit {

  @Input() percent: number = 0;

  completedWidth: string;
  
  constructor() { }

  ngOnInit() {
    this.completedWidth = this.percent + "%";
  }

}
