import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';

import { LayoutService } from '../../../core/layout.service';
import { NavigationService } from '../../../app-routing/navigation.service';

@Component({
  selector: 'ebx-biz-dim-layout-switch',
  templateUrl: './biz-dim-layout-switch.component.html',
  styleUrls: ['./biz-dim-layout-switch.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class BizDimLayoutSwitchComponent implements OnInit {

  layoutOptions: any;

  @Input() dimQId: string;
  @Input() recordIdentity: string;

  activeOptId: string;

  constructor(private layoutService: LayoutService,
    private navSerice: NavigationService,
    private route: ActivatedRoute) { }

  ngOnInit() {
    
    this.layoutOptions = this.layoutService.getDimensionLayoutOptions(this.dimQId);
    
    this.activeOptId = this.route.snapshot.queryParams['layout'];
    if (!this.activeOptId) {
      this.activeOptId = 'default';
    }
  }

  switchLayout(opt: any) {
    this.navSerice.goToDimDetail(this.dimQId, this.recordIdentity, this.getLayoutQueryParams(opt));
  }

  getLayoutQueryParams(opt: any) {
    return { layout: opt.id };
  }

}
