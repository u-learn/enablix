import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs/Observable';

import { ContentTemplateService } from '../../core/content-template.service';
import { Utility } from '../../util/utility';

@Component({
  selector: 'ebx-company-properties',
  templateUrl: './company-properties.component.html',
  styleUrls: ['./company-properties.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class CompanyPropertiesComponent implements OnInit {

  propContainers: any[];
  filteredContainers: Observable<any>;

  nameCtrl: FormControl;

  constructor(private ctService: ContentTemplateService) { }

  ngOnInit() {
    this.propContainers = this.ctService.templateCache.refDataContainers;
    Utility.sortArrayByLabel(this.propContainers);

    this.nameCtrl = new FormControl();
    this.filteredContainers = this.nameCtrl.valueChanges.startWith(null)
        .map(ct => ct ? this.filterContainers(ct) : this.propContainers);
  }


  filterContainers(ct: string) {
    return this.propContainers.filter(cont =>
      (cont.label.toLowerCase().indexOf(ct.toLowerCase()) >= 0)
        || (cont.singularLabel && cont.singularLabel.toLowerCase().indexOf(ct.toLowerCase()) >= 0));
  }

}
