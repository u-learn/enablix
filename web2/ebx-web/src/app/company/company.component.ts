import { Component, OnInit, ViewEncapsulation } from '@angular/core';

import { TenantService } from '../services/tenant.service';

@Component({
  selector: 'ebx-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CompanyComponent implements OnInit {

  constructor(public tenantService: TenantService) { }

  ngOnInit() {
  }

}
