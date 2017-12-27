import { Component, OnInit, ViewEncapsulation, Input, forwardRef } from '@angular/core';
import { FormControl, ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

import { ContentItem } from '../../../model/content-item.model';

@Component({
  selector: 'ebx-rich-text-input',
  templateUrl: './rich-text-input.component.html',
  styleUrls: ['./rich-text-input.component.css'],
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => RichTextInputComponent),
      multi: true,
    }
  ]
})
export class RichTextInputComponent implements OnInit, ControlValueAccessor {

  @Input() record: any;
  @Input() contentItem: ContentItem;

  textCtrl: FormControl;
  disabled: boolean = false;

  constructor() {
    this.textCtrl = new FormControl();
  }

  ngOnInit() {
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
    this.disabled = isDisabled;
  }

}