import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter, AfterViewInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/startWith';

import { ContentItem } from '../../../model/content-item.model';
import { ContentTemplateService } from '../../content-template.service';
import { Tag } from '../content-tags.component';
import { Container } from '../../../model/container.model';

@Component({
  selector: 'ebx-add-content-tags',
  templateUrl: './add-content-tags.component.html',
  styleUrls: ['./add-content-tags.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class AddContentTagsComponent implements OnInit, AfterViewInit {

  @Input() tagItems: ContentItem[];
  @Output() onAddItem = new EventEmitter<Tag>();
  @Output() onClose = new EventEmitter<void>();

  objectInputCtrl: FormControl;
  itemInputCtrl: FormControl;

  selectContentItem: ContentItem;
  tagItemValues: any = {};

  filteredObjectTags: Observable<ContentItem[]>;
  filteredItemTags: Observable<Tag[]>;

  constructor(private contentTemplateService: ContentTemplateService) { 
    this.objectInputCtrl = new FormControl();
    this.itemInputCtrl = new FormControl();
  }

  ngOnInit() {
    
    if (this.tagItems) {
      
      this.tagItems.forEach(tagItem => {

        this.contentTemplateService.getBoundedValueList(tagItem)
            .subscribe(
                result => {

                  this.tagItemValues[tagItem.id] =
                      result.map(val => {
                        
                        let container = this.getItemContainer(tagItem);

                        let tag: Tag = {
                          id: val.id,
                          label: val.label,
                          recordPropId: tagItem.id,
                          containerQId: container ? container.qualifiedId : null
                        };

                        return tag;
                      });
                }
              );
      });

      this.filteredObjectTags = this.objectInputCtrl.valueChanges.startWith(null)
        .map(ct => ct ? this.filterContentItems(ct) : this.tagItems);
    }
  }

  ngAfterViewInit() {
  }

  selectItem(contentItem: ContentItem) {
    this.selectContentItem = contentItem;
    this.itemInputCtrl.setValue(null);
    this.filteredItemTags = this.itemInputCtrl.valueChanges.startWith(null)
        .map(ct => ct ? this.filterTags(ct) : this.tagItemValues[this.selectContentItem.id]);
  }

  clearContentItemSelection() {
    this.selectContentItem = null;
  }

  getItemContainer(item: ContentItem) : Container {
    return this.contentTemplateService.templateCache.getBoundedItemDatastoreContainer(item);
  }

  getItemColor(contentItem: ContentItem) : string {
    return this.contentTemplateService.templateCache.getBoundedContentItemColor(contentItem);
  }

  addItemVal(itemVal: any) {
    this.onAddItem.emit(itemVal);
  }

  filterTags(t: string) : Tag[] {
    return this.tagItemValues[this.selectContentItem.id].filter(tag =>
      tag.label.toLowerCase().indexOf(t.toLowerCase()) >= 0)
  }

  filterContentItems(t: string) : ContentItem[] {
    return this.tagItems.filter(tag =>
      tag.label.toLowerCase().indexOf(t.toLowerCase()) >= 0)
  }

  close() {
    this.onClose.emit();
  }

}
