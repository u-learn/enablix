import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';

import { ContentTemplateService } from '../core/content-template.service';
import { ContentService } from '../core/content/content.service';
import { AlertService } from '../core/alert/alert.service';
import { NavigationService } from '../app-routing/navigation.service';

@Component({
  selector: 'ebx-data-card',
  templateUrl: './data-card.component.html',
  styleUrls: ['./data-card.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class DataCardComponent implements OnInit {

  @Input() record? : any;
  @Input() recordIdentity? : string;
  @Input() containerQId : string;

  isContentRecord = false;
  title: string;
  text: string;
  containerLabel: string;
  thumbnailUrl: string;
  linkedContentText: string;
  linkedContentCount: number;

  cardColor: string;

  constructor(
    private contentTemplateService: ContentTemplateService, 
    private contentService: ContentService,
    private alert: AlertService, private navService: NavigationService) { }

  ngOnInit() {
    
    if (!this.record) {
      
      if (this.recordIdentity) {
      
        this.contentService.getContentRecord(this.containerQId, this.recordIdentity)
            .subscribe(
                data => {
                  this.record = data;
                  this.recordIdentity = this.record.identity;
                  this.initState();
                },
                error => {
                  this.alert.error("Error fetching record data.");
                }
              );
      }

    } else {
      this.recordIdentity = this.record.identity;
      this.initState();
    }

  }

  private initState() {
    
    let container = this.contentTemplateService.getConcreteContainerByQId(this.containerQId);

    if (container != null) {
      this.isContentRecord = this.contentTemplateService.isBusinessContent(container);
      this.contentService.decorateRecord(container, this.record);

      this.title = this.record.__title;
      this.containerLabel = container.label.toUpperCase();
      this.cardColor = container.color ;

      if (this.record.__decoration) {
        let decoration = this.record.__decoration;
        this.thumbnailUrl = decoration.__thumbnailUrl;
        this.linkedContentText = decoration.__linkedContentText;
        this.linkedContentCount = decoration.__linkedContentCount;
        this.text = decoration.__textContent;  
      }
      
    }
  }

  navToBizContent() {
    this.navService.goToContentDetail(this.containerQId, this.recordIdentity);
  }

  navToBizDimension() {
    this.navService.goToDimDetail(this.containerQId, this.recordIdentity);
  }

}
