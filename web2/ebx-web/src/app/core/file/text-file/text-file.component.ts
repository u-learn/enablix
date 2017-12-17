import { Component, OnInit, ViewEncapsulation, Input, forwardRef } from '@angular/core';
import { FormControl, ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  selector: 'ebx-text-file',
  templateUrl: './text-file.component.html',
  styleUrls: ['./text-file.component.scss'],
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => TextFileComponent),
      multi: true,
    }
  ]
})
export class TextFileComponent implements OnInit, ControlValueAccessor {

  data: any;
  fileCtrl: FormControl;

  constructor() { 
  }

  ngOnInit() {
  }

  onChange = (val: any) => {};

  onTouched = () => {}

  writeValue(val: any): void {
    this.data = val;
  }

  registerOnChange(fn: (val: any) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState(isDisabled: boolean): void {
    this.fileCtrl.disable();
  }

  onFileChange(event) {
    
    let reader = new FileReader();

    reader.onload = (loadEvent: any) => {
      this.data = {
        name: event.target.files[0].name,
        size: event.target.files[0].size,
        type: event.target.files[0].type,
        data: loadEvent.target.result
      }

      this.onChange(this.data);
    }
    
    reader.readAsText(event.target.files[0]);
  }

}
