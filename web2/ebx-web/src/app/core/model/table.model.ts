import { Observable } from 'rxjs/Observable';

import { DataType } from '../data-search/filter-metadata.model';

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

  confirmConfig?: ActionConfirmConfig<T>;

  execute: (selectedRecs: T[]) => Observable<any>;
}

export interface TableActionConfig<T> {
  getAvailableActions(selectedRecords: T[]) : TableAction<T>[];
}

export class ActionConfirmConfig<T> {
  title: string;
  okLabel: string;
  cancelLabel: string;
  confirmMsg: (selectedRecs: T[]) => string;
}
