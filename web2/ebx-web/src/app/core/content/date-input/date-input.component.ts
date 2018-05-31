import { Component, OnInit, ViewEncapsulation, Input, forwardRef } from '@angular/core';
import { FormControl, ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

import { ContentItem } from '../../../model/content-item.model';

@Component({
  selector: 'ebx-date-input',
  templateUrl: './date-input.component.html',
  styleUrls: ['./date-input.component.scss'],
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => DateInputComponent),
      multi: true,
    }
  ]
})
export class DateInputComponent implements OnInit {

  @Input() record: any;
  @Input() contentItem: ContentItem;

  dateCtrl: FormControl;
  disabled: boolean = false;

  constructor() {
    this.dateCtrl = new FormControl();
  }

  ngOnInit() {
  }

  onChange = (date: any) => {};

  onTouched = () => {}

  writeValue(date: any): void {
    this.dateCtrl.setValue(new Date(date));
  }


  registerOnChange(fn: (date: any) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.disabled = isDisabled;
  }

}
