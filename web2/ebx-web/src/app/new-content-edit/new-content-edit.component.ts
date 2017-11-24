import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';

import { NewContentService } from '../core/content/new-content.service';
import { NewContent } from '../model/new-content.model';
import { Container } from '../model/container.model';
import { ContentItem } from '../model/content-item.model';
import { SelectOption } from '../core/select/select.component';
import { ContentTemplateService } from '../core/content-template.service';


@Component({
  selector: 'ebx-new-content-edit',
  templateUrl: './new-content-edit.component.html',
  styleUrls: ['./new-content-edit.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class NewContentEditComponent implements OnInit {

  record: any = {};
  container: Container;
  inputItems: ContentItem[] = [];

  contentTypes: SelectOption[];
  contentTypeCtrl: FormControl;

  constructor(private route: ActivatedRoute, 
              private newContentService: NewContentService,
              private contentTemplateService: ContentTemplateService) { 
    
    this.contentTypeCtrl = new FormControl({disabled: true});
  }

  ngOnInit() {
    
    const newContent = this.newContentService.getNewContent();
    console.log(newContent);

    if (newContent) {

      this.record = newContent.data;
      this.container = newContent.container;
      this.initState();
      
    } else {
      
      this.record = {};
      
      this.route.params.subscribe(params => {
        let containerQId = params['cQId'];
        this.container = this.contentTemplateService.getContainerByQId(containerQId);
        this.initState();
      });
      
    }
  
  }

  private initState() {

    if (this.container) {
      
      this.contentTypes = this.contentTemplateService.getBizContentSelectOptions();
      this.contentTypes.sort((a, b) => (a.label > b.label ? 1 : -1));    

      let selectedType: SelectOption[] = [{id: this.container.qualifiedId, label: this.container.label}];
      this.contentTypeCtrl.setValue(selectedType);

      this.inputItems = 
          this.contentTemplateService.templateCache.getFreeInputContentItems(this.container)
              .filter(item => this.container.titleItemId != item.id);
    }
  }

}
