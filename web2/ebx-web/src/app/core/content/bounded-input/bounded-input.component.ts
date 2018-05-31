import { Component, OnInit, ViewEncapsulation, Input, forwardRef } from '@angular/core';
import { FormControl, ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

import { ContentItem } from '../../../model/content-item.model';
import { SelectOption } from '../../select/select.component';
import { ContentTemplateService } from '../../content-template.service'; 

@Component({
  selector: 'ebx-bounded-input',
  templateUrl: './bounded-input.component.html',
  styleUrls: ['./bounded-input.component.css'],
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => BoundedInputComponent),
      multi: true,
    }
  ]
})
export class BoundedInputComponent implements OnInit, ControlValueAccessor {

  @Input() record: any;
  @Input() contentItem: ContentItem;
  @Input() placeholder?: string;

  multiValued: boolean = false;

  selectCtrl: FormControl;
  options: SelectOption[] = [];
  boundedItemColor: string;

  constructor(private ctService: ContentTemplateService) {
    this.selectCtrl = new FormControl();
  }

  ngOnInit() {

    if (!this.placeholder) {
      this.placeholder = this.contentItem.label;
    }
    
    this.ctService.getBoundedValueList(this.contentItem).subscribe(
        res => {
          this.options = res;
        }, 
        err => {
          console.log("Error getting bounded list value.");
        }
      );

    this.multiValued = this.contentItem.bounded.multivalued;

    this.boundedItemColor = this.ctService.templateCache.getBoundedContentItemColor(this.contentItem);
    this.selectCtrl.valueChanges.subscribe(val => this.onChange(val));
  }

  onChange = (opts: SelectOption[]) => {};

  onTouched = () => {}

  writeValue(opts: SelectOption[]): void {
    this.selectCtrl.setValue(opts);
  }


  registerOnChange(fn: (opts: SelectOption[]) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.selectCtrl.disable();
  }

}
