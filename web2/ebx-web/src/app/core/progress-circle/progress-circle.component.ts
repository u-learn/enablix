import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

@Component({
  selector: 'ebx-progress-circle',
  templateUrl: './progress-circle.component.html',
  styleUrls: ['./progress-circle.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ProgressCircleComponent implements OnInit {

  @Input() percent: number = 0;

  constructor() { }

  ngOnInit() {
  }

  progressPercentClazz() {
    return "progress-" + Math.floor(this.percent);
  }

}
