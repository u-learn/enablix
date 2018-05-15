import { Component, OnInit, ViewEncapsulation, Input, forwardRef, ViewChild, Renderer2, AfterViewInit } from '@angular/core';
import { FormControl, ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { Observable } from 'rxjs/Observable';
import { Subject } from 'rxjs/Subject';
import { BehaviorSubject } from 'rxjs/BehaviorSubject';
import 'rxjs/add/operator/withLatestFrom';
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
export class SelectComponent implements OnInit, ControlValueAccessor, AfterViewInit {

  @Input() headIcon?: string;
  _options: SelectOption[] = [];
  
  @Input() multi?: boolean = false;
  @Input() placeholder?: string = "";
  @Input() pillsColor?: string;

  @Input() allowNewEntry: boolean = false;

  @ViewChild("textInput") textInput;

  optionsVisible: boolean;
  singleValue: string = "";
  disabled: boolean = false;

  selected: SelectOption[] = [];
  selectCtrl: FormControl;

  filteredOptions = new BehaviorSubject<SelectOption[]>([]);
  upDownEvents = new Subject<string>();
  highlightedOpt: SelectOption;

  constructor(private renderer: Renderer2) { 
    this.optionsVisible = false;
    this.selectCtrl = new FormControl();
  }

  @Input() 
  set options(opts: SelectOption[]) {
    this._options = opts;
    this.initSearchInput();
  }

  initSearchInput() {
    let sub = this.selectCtrl.valueChanges.startWith(null).share();
    sub.map(ct => {
      this.highlightedOpt = null;
      if (ct) {
        this.showOptions();
      }
      return ct ? this.filterOptions(ct) : this._options
    }).subscribe(this.filteredOptions);
  }

  ngAfterViewInit(): void {

    this.upDownEvents.withLatestFrom(this.filteredOptions)
        .subscribe(([evt, opts]) => {
          
          if (opts && opts.length > 0) {

            if (evt === 'down') {

              if (!this.highlightedOpt) {
                this.highlightedOpt = opts[0]
              } else {
                let currHighlightIndx = this.getHighlightedOptIndex(opts);
                if (currHighlightIndx >= 0 && currHighlightIndx < opts.length - 1) {
                  this.highlightedOpt = opts[currHighlightIndx + 1];
                }
              }

            } else if (evt === 'up') {

              if (this.highlightedOpt) {

                let currHighlightIndx = this.getHighlightedOptIndex(opts);
                if (currHighlightIndx == 0) {
                  this.highlightedOpt = null;
                } else if (currHighlightIndx > 0) {
                  this.highlightedOpt = opts[currHighlightIndx - 1];
                }
              }
            }

          } else {
            this.highlightedOpt = null;
          }

          console.log(this.highlightedOpt);
          
          if (this.highlightedOpt) {
            // bring to focus
            var hgElem = document.getElementById(this.highlightedOpt.id);
            if (hgElem) {
              hgElem.scrollIntoView();
            }
          }

        });
  }

  getHighlightedOptIndex(opts: any) {
    let currHighlightIndx = -1;
    for (var i = 0; i < opts.length; i++) {
      let o = opts[i];
      if (o.id == this.highlightedOpt.id) {
        currHighlightIndx = i;
        break;
      }
    }
    return currHighlightIndx;
  }

  selectHighlighted(event) {
    
    if (this.highlightedOpt) {
      this.updateSelection(this.highlightedOpt);
      this.highlightedOpt = null;
    }

    event.preventDefault();
  }

  onBlur() {
    //console.log("focus lost - " + this.selectCtrl.value);
    
    if (this.allowNewEntry) {
    
      setTimeout(() => {
        //console.log("focus timeout - " + this.selectCtrl.value);
        
        let ct = this.selectCtrl.value;
        if (ct && ct.trim().length > 1) {
        
          let opt: SelectOption = {id: ct.trim(), label: ct.trim()};
          if (!this.selectedOptionsContain(opt)) {
            this.selected.push(opt);
            this.selectCtrl.setValue(null, {emitEvent: false});
            this.onChange(this.selected);
          } 
        }
      }, 300);  

    }
    
  }

  highlightOnUpArrow() {
    this.upDownEvents.next('up');
  }

  highlightOnDownArrow() {
    this.upDownEvents.next('down');
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

        this.selectCtrl.setValue(null);
        
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

  showOptions(clearText?: boolean) {
    
    if (clearText) {
      this.selectCtrl.setValue(null);
    }

    if (this.options && this.options.length > 0) {
      this.optionsVisible = true;
    }
  }

  removeOption(indx: number) {
    if (indx != -1) {
      this.selected.splice(indx, 1);
    }
  }

  onKeypress(event) {
    console.log("Key press");
  }

  updateSelection(opt: SelectOption) {
    //console.log("update selection: " + opt.label);
    this.addSelectedOption([opt]);
    if (!this.multi) {
      this.hideOptions();
    }
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
