import { Observable } from 'rxjs/Observable';

import { DataType } from '../core/data-search/filter-metadata.model';

export class TableColumn {
  heading: string;
  key: string;

  sortProp?: string;
  dataType?: DataType;
  headerCssClass?: string;
}

export class TableAction<T> {
  
  label: string;
  iconClass: string;
  successMessage?: string;

  execute: (selectedRecs: T[]) => Observable<any>;
}

export interface TableActionConfig<T> {
  getAvailableActions(selectedRecords: T[]) : TableAction<T>[];
}