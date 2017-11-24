import { Component, OnInit, ViewEncapsulation, Input, forwardRef } from '@angular/core';
import { FormControl, ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

import { ContentItem } from '../../../model/content-item.model';

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

  textCtrl: FormControl;
  disabled: boolean = false;

  constructor() {
    this.textCtrl = new FormControl();
  }

  ngOnInit() {
    this.textCtrl.valueChanges.subscribe(val => {
      this.onChange(val);
    });
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
