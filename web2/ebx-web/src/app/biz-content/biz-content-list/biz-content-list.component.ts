import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { Container } from '../../model/container.model';
import { ContentTemplateService } from '../../core/content-template.service';
import { SearchBarService } from '../../search-bar/search-bar.service';
import { ContentService } from '../../core/content/content.service';
import { DataPage } from '../../model/data-page.model';
import { AlertService } from '../../core/alert/alert.service';

@Component({
  selector: 'ebx-biz-content-list',
  templateUrl: './biz-content-list.component.html',
  styleUrls: ['./biz-content-list.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class BizContentListComponent implements OnInit {

  container: Container;
  dataPage: DataPage;

  constructor(private route: ActivatedRoute, 
    private contentTemplateService: ContentTemplateService,
    private contentService: ContentService,
    private alert: AlertService,
    private sbService: SearchBarService) { }

  ngOnInit() {
    this.route.params.subscribe(params => {

      var containerQId = params['cQId'];

      if (containerQId) {

        this.container = this.contentTemplateService.getContainerByQId(containerQId);
        this.sbService.setBizContentListSearchBar(this.container);

        this.contentService.getAllRecords(containerQId, 50).subscribe(
            result => {
               this.dataPage = result;
            },
            error => {
              this.alert.error("Error fetching content records. Please try again later."); 
            }
          );
      }
    });
  }

}
