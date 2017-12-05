import { Component, OnInit, ViewEncapsulation, Input, ContentChild, TemplateRef, EventEmitter, Output } from '@angular/core';

import { TableColumn } from '../model/table.model';
import { Pagination, Direction } from '../model/pagination.model';

@Component({
  selector: 'ebx-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TableComponent implements OnInit {

  @Input() tableData: any;
  @Input() tableColumns: TableColumn[];
  @Input() pagination?: Pagination;

  @Output() onRefreshData = new EventEmitter<void>();

  @ContentChild(TemplateRef) rowTemplate: TemplateRef<any>;

  constructor() { }

  ngOnInit() {
  }

  getDirectionClass(col: TableColumn) {
    if (this.pagination && this.pagination.sort.field == col.sortProp) {
      return this.pagination.sort.direction == Direction.ASC ? "dir-up" : "";
    }
  }

  isSortedOnColumn(col: TableColumn) {
    return this.pagination && this.pagination.sort.field == col.sortProp;
  }

  sortIcon(col: TableColumn) {
    let icon = "/assets/images/icons/dropdown.svg";
    if (this.isSortedOnColumn(col)) {
      icon = "/assets/images/icons/dropdown-blue.svg";
    }
    return icon;
  }

  sortData(col: TableColumn) {
    
    let sortDir = Direction.ASC;
    if (this.isSortedOnColumn(col) && this.pagination.sort.direction == Direction.ASC) {
      sortDir = Direction.DESC;
    }

    this.pagination.sort.field = col.sortProp;
    this.pagination.sort.direction = sortDir;

    this.onRefreshData.emit();
  }

}
