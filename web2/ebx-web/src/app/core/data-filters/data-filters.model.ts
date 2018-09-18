import { Observable } from 'rxjs/Observable';
import { } from ''

export class DataFilter {
  id: string;
  type: string;
  name: string;
  options: { [key: string]: any };
  masterList: () => Observable<any>;
  defaultValue: () => any;
  validator?: DataFilterValidator
  valueTx: DataFilterValueTx 
}

export interface DataFilterValidator {
  validate(selectedValue: any) : DataFilterError[];
}

export class DataFilterError {
  message: string;
}

export interface DataFilterValueTx {
  transform(selectedValue: any) : any;
}

export class DataFiltersOptions {
  
  heading?: string;
  resetLabel?: string;
  searchLabel?: string;
  hideFiltersWithNoOptions?: boolean;
  persistSelection?: boolean;
  showSaveAsDefault?: boolean;
  prefValuesKey?: string;
  searchOnLoad?: boolean;

}

export class DataFiltersConfig {
  filters: DataFilter[];
  options: DataFiltersOptions;
}

export class PassThroughDataFilterValidator implements DataFilterValidator {

  static theInstance = new PassThroughDataFilterValidator();

  validate(selValues: any) : DataFilterError[] {
    return null;
  }
}

export class AtleastOneValueDataFilterValidator implements DataFilterValidator {

  errMsg: string;

  constructor(errMsg: string) {
    this.errMsg = errMsg;
  }

  validate(selValues: any) : DataFilterError[] {
    if (selValues && selValues.length == 0) {
      return [{ message: this.errMsg }];
    }
    return null;
  } 

}

export class FixedDataFilterValueProxyTx implements DataFilterValueTx {
  
  filterVal: any;
  valueTx: DataFilterValueTx;

  constructor(valueTx: DataFilterValueTx, filterVal: any) {
    this.valueTx = valueTx;
    this.filterVal = filterVal;
  }
  
  transform(selectedValue: any) : any {
    return this.valueTx.transform(this.filterVal);
  }

}

export class IdPropDataFilterValueTx implements DataFilterValueTx {
  
  static theInstance: IdPropDataFilterValueTx = new IdPropDataFilterValueTx();

  transform(selectedValues: any) : any {
    
    if (selectedValues && selectedValues.length > 0) {
    
      var returnVal = [];
      selectedValues.forEach((val) => {
        returnVal.push(val.id);
      });

      return returnVal;
    }
    
    return null;
  }
}

export class AllOptionFilterValueTx implements DataFilterValueTx {
  
  filterOptions: any;
  constructor(filterOptions: any) {
    this.filterOptions = filterOptions;
  }

  transform(selectedValues: any) : any {

    if (selectedValues && selectedValues.length > 0) {
    
      var returnVal = [];

      for(var i = 0; i < selectedValues.length; i++) {
      
        var selOpt = selectedValues[i];
        if (selOpt.id == '~all~') {
          returnVal = this.getAllValues();
          break;
        } else {
          returnVal.push(selOpt.id);
        }

      }

      return returnVal;
    }

    return null;
  }

  getAllValues() {
    var returnVal = [];
    this.filterOptions.forEach((val) => {
      if (val.id != '~all~') {
        returnVal.push(val.id);
      }
    });
    return returnVal;
  }
}

export class ClientSideOnlyFilterValueTx implements DataFilterValueTx {
  
  static theInstance = new ClientSideOnlyFilterValueTx();

  transform(selectedValues: any) : any {
    // this will be a client side filtering, so do not send any value to server
    return null;
  }
}