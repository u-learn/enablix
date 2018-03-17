import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { RecoContentService } from './reco-content.service';
import { AlertService } from '../core/alert/alert.service';

@Component({
  selector: 'ebx-reco-content',
  templateUrl: './reco-content.component.html',
  styleUrls: ['./reco-content.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class RecoContentComponent implements OnInit {

  recoList: any;

  constructor(private recoContentService: RecoContentService,
    private alert: AlertService) { }

  ngOnInit() {
    this.recoContentService.fetchRecoContent()
        .subscribe(
            result => {
              this.recoList = result;
            },
            error => {
              this.alert.error("Error fetching recommended content.", error.status);
            }
          );
  }

}
