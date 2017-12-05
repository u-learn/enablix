import { DataType } from '../core/data-search/filter-metadata.model';

export class TableColumn {
  heading: string;
  key: string;
  
  sortProp?: string;
  dataType?: DataType;
  headerCssClass?: string;
}