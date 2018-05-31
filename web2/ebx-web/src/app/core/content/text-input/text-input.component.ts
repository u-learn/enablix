import { Component, OnInit, ViewEncapsulation, Input, forwardRef } from '@angular/core';
import { FormControl, ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

import { ContentItem } from '../../../model/content-item.model';
import { ContentTemplateCache } from '../../content-template-cache';
import { Utility } from '../../../util/utility';

@Component({
  selector: 'ebx-text-input',
  templateUrl: './text-input.component.html',
  styleUrls: ['./text-input.component.css'],
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => TextInputComponent),
      multi: true,
    }
  ]
})
export class TextInputComponent implements OnInit, ControlValueAccessor {

  @Input() record: any;
  @Input() contentItem: ContentItem;
  @Input() contentTemplate: ContentTemplateCache;

  textCtrl: FormControl;
  disabled: boolean = false;

  constructor() {
    this.textCtrl = new FormControl();
  }

  ngOnInit() {
    
    this.textCtrl.valueChanges.subscribe(val => {
      if (val) {
        this.autoPopulateDependents(val);
      }
      this.onChange(val);
    });

    var autooff = !Utility.isNullOrUndefined(this.record[this.contentItem.id]);
    this.setAutoPopulateOff(this.contentItem.id, autooff);
  }

  onNativeInputChange() {
    this.setAutoPopulateOff(this.contentItem.id, true);
  }

  setAutoPopulateOff(itemId: string, flagVal: boolean) {
    if (!this.record.__decoration) {
      this.record.__decoration = {
        '__autoPopulateOff': {}
      };
    }

    if (!this.record.__decoration.__autoPopulateOff) {
      this.record.__decoration['__autoPopulateOff'] = {};
    }

    this.record.__decoration['__autoPopulateOff'][itemId] = flagVal;
  }

  autoPopulateDependents(val) {
    var dependentIds = this.contentTemplate.getAutoPopulateDependentItemIds(this.contentItem.qualifiedId);
    if (dependentIds) {
      dependentIds.forEach((itemId) => {
        if (this.canAutoPopulate(itemId)) {
          this.record[itemId] = val;
        }
      });
    }
  }

  canAutoPopulate(itemId: string) {
    return !this.isAutoPopulateOff(itemId, 'autooff');
  }

  isAutoPopulateOff(itemId: string, flagProp: string) {
    return this.record.__decoration && this.record.__decoration.__autoPopulateOff
            && this.record.__decoration.__autoPopulateOff[itemId];
  }

  onChange = (text: string) => {};

  onTouched = () => {}

  writeValue(text: string): void {
    this.textCtrl.setValue(text);
  }


  registerOnChange(fn: (text: string) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.textCtrl.disable();
  }

}
