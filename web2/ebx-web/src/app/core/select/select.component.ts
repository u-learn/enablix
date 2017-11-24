import { Component, OnInit, ViewEncapsulation, Input, forwardRef, ViewChild, Renderer2 } from '@angular/core';
import { FormControl, ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/startWith';

import { Constants } from '../../util/constants';

@Component({
  selector: 'ebx-select',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.scss'],
  encapsulation: ViewEncapsulation.None,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => SelectComponent),
      multi: true,
    }
  ]
})
export class SelectComponent implements OnInit, ControlValueAccessor {

  @Input() headIcon?: string;
  _options: SelectOption[] = [];
  
  @Input() multi?: boolean = false;
  @Input() placeholder?: string = "";
  @Input() pillsColor?: string;

  @ViewChild("textInput") textInput;

  optionsVisible: boolean;
  singleValue: string = "";
  disabled: boolean = false;

  selected: SelectOption[] = [];
  selectCtrl: FormControl;
  filteredOptions: Observable<SelectOption[]>;

  constructor(private renderer: Renderer2) { 
    this.optionsVisible = false;
    this.selectCtrl = new FormControl();
    this.filteredOptions = this.selectCtrl.valueChanges.startWith(null)
        .map(ct => ct ? this.filterOptions(ct) : this.options);
  }

  @Input() 
  set options(opts: SelectOption[]) {
    this._options = opts;
    this.filteredOptions = this.selectCtrl.valueChanges.startWith(null)
        .map(ct => ct ? this.filterOptions(ct) : this._options);
  }

  get options() : SelectOption[] {
    return this._options;
  }

  ngOnInit() {
    if (!this.pillsColor) {
      this.pillsColor = Constants.defaultTypeColor;
    }
  }

  private selectedOptionsContain(opt: SelectOption) : boolean {
    return this.selectedOptionsIndex(opt) != -1;
  }

  private selectedOptionsIndex(opt: SelectOption) : number {
    for (let i = 0; i < this.selected.length; i++) {
      if (opt.id == this.selected[i].id) {
        return i;
      }
    }

    return -1;
  }

  onChange = (opts: SelectOption[]) => {};

  onTouched = () => {}

  writeValue(opts: SelectOption[]): void {
    this.selected = [];
    this.addSelectedOption(opts);
  }

  private addSelectedOption(opts: SelectOption[]) {
    
    if (opts && opts.length > 0) {
      
      if (this.multi) {
        opts.forEach(opt => {
          if (!this.selectedOptionsContain(opt)) {
            this.selected.push(opt);
          }  
        });
        
      } else {
        let opt: SelectOption = opts[0];
        this.selected = [opt];
        this.selectCtrl.setValue(opt.label);
      }

      this.onChange(this.selected);
    }
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

  hideOptions() {
    this.optionsVisible = false;
  }

  showOptions() {
    if (this.options && this.options.length > 0) {
      this.optionsVisible = true;
    }
  }

  removeOption(indx: number) {
    if (indx != -1) {
      this.selected.splice(indx, 1);
    }
  }

  updateSelection(opt: SelectOption) {
    this.addSelectedOption([opt]);
    this.hideOptions();
  }

  filterOptions(ct: string) {
    return this.options.filter(opt =>
      opt.label.toLowerCase().indexOf(ct.toLowerCase()) >= 0);
  }

}

export class SelectOption {
  id: string;
  label: string;
}
