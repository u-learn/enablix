import { Component, OnInit, ViewEncapsulation, Input } from '@angular/core';
import { Router } from '@angular/router';

import { ContentTemplateService } from '../core/content-template.service';
import { ContentService } from '../core/content/content.service';
import { AlertService } from '../core/alert/alert.service';
import { NavigationService } from '../app-routing/navigation.service';
import { Container } from '../model/container.model';

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

  container: Container;

  isBizContent = false;
  title: string;
  containerLabel: string;
  linkedContentText: string;
  linkedContentCount: number;

  cardColor: string;

  constructor(
    private contentTemplateService: ContentTemplateService, 
    private contentService: ContentService,
    private alert: AlertService, private navService: NavigationService,
    private router: Router) { }

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
                  this.alert.error("Error fetching record data.", error.status);
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
      this.container = container;
      this.isBizContent = this.contentTemplateService.isBusinessContent(container);
      this.contentService.decorateRecord(container, this.record);

      this.title = this.record.__title;
      this.containerLabel = container.singularLabel.toUpperCase();
      this.cardColor = container.color ;

      if (this.record.__decoration) {
        let decoration = this.record.__decoration;
        this.linkedContentText = decoration.__linkedContentText;
        this.linkedContentCount = decoration.__linkedContentCount;
      }
      
    }
  }

  navToBizContent() {
    this.navService.goToContentDetail(this.containerQId, this.recordIdentity, {}, this.router.url);
  }

  navToBizDimension() {
    this.navService.goToDimDetail(this.containerQId, this.recordIdentity);
  }

  navToDimList() {
    this.navService.goToDimList(this.containerQId);
  }

  navToContentList() {
    this.navService.goToContentList(this.containerQId);
  }

  

}
