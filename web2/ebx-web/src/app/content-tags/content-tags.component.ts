import { Component, OnInit, ViewEncapsulation, Input, ViewChild } from '@angular/core';

import { ContentItem } from '../model/content-item.model';
import { Container } from '../model/container.model';
import { ContentTemplateService } from '../core/content-template.service';
import { Constants } from '../util/constants';
import { AddContentTagsComponent } from './add-content-tags/add-content-tags.component';
import { ContentService } from '../core/content/content.service';

@Component({
  selector: 'ebx-content-tags',
  templateUrl: './content-tags.component.html',
  styleUrls: ['./content-tags.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class ContentTagsComponent implements OnInit {

  _record: any;
  @Input() tagItems?: ContentItem[];
  @Input() editing?: boolean = false;

  tags: Tag[] = [];

  addTagsVisible: boolean = false;

  constructor(private contentTemplateService: ContentTemplateService,
              private contentService: ContentService) { }

  @Input() 
  set record(rec: any) {
    this._record = rec;
    this.initState();
  }

  get record() {
    return this._record;
  }


  initState() {

    this.tags = [];

    if (this._record) {

      if (this.tagItems) {

        this.tagItems.forEach(tagItem => {
        
          let tagValues = this.record[tagItem.id];
        
          if (tagValues && tagValues[0]) {
        
            let tagContainer = 
              this.contentTemplateService.templateCache
                  .getBoundedItemDatastoreContainer(tagItem);

            tagValues.forEach(tagVal => {
                this.tags.push({
                    id: tagVal.id, 
                    label: tagVal.label, 
                    recordPropId: tagItem.id,
                    containerQId: tagContainer ? tagContainer.qualifiedId : null
                  });
              });
          }

        });
      }
    }
  }

  ngOnInit() {
  }

  removeTag(tag: Tag) {
    let existValues = this.record[tag.recordPropId];
    this.removeTagFrom(tag, existValues);
    this.removeTagFrom(tag, this.tags);
  }

  removeTagFrom(tag: Tag, fromList: any) {
    if (fromList && fromList[0]) {
      let index = this.indexOfTag(fromList, tag);
      if (index >= 0) {
        fromList.splice(index, 1);
      }
    }
  }

  hasValue() : boolean {
    return this.contentService.attrHasValue(this.tags);
  }

  addTag(tag: Tag) {
    
    let existValues = this.record[tag.recordPropId];
    
    if (!existValues) {
      
      this.record[tag.recordPropId] = [];
      this.record[tag.recordPropId].push({
        id: tag.id,
        label: tag.label
      });

      this.tags.push(tag);

    } else {

      if (this.indexOfTag(existValues, tag) == -1) { // does not exist

        existValues.push({
          id: tag.id,
          label: tag.label
        });

        this.tags.push(tag);
      }
    }
  }

  private indexOfTag(tagValues: any, tag: Tag) {
    
    let tagIndex = -1;

    for (let i = 0; i < tagValues.length; i++) {
      if (tagValues[i].id == tag.id) {
        tagIndex = i;
        break;
      } 
    }

    return tagIndex;
  }

  showAddTags() {
    this.addTagsVisible = true;
  }

  hideAddTags() {
    this.addTagsVisible = false;
  }

}


export class Tag {
  id: string;
  label: string;
  recordPropId: string;
  containerQId: string;
}
