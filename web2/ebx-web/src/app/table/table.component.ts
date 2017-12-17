import { Component, OnInit, ViewEncapsulation, Input, ContentChild, TemplateRef, EventEmitter, Output } from '@angular/core';

import { TableColumn, TableActionConfig, TableAction } from '../model/table.model';
import { Pagination, Direction } from '../model/pagination.model';

@Component({
  selector: 'ebx-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class TableComponent implements OnInit {

  _tableData: any;
  @Input() tableColumns: TableColumn[];
  @Input() pagination?: Pagination;
  @Input() tableActionConfig?: TableActionConfig<any>;

  @Output() onRefreshData = new EventEmitter<void>();

  @ContentChild('dataRow') rowTemplate: TemplateRef<any>;

  availableActions: TableAction<any>[];
  selectedRecords: any[];

  rowSelectOptions: RowSelectOption[];

  constructor() { }

  ngOnInit() {
    this.clearSelection();
  }

  @Input()
  set tableData(tbData: any) {
    
    this._tableData =tbData;
    this.rowSelectOptions = [];

    if (tbData && tbData.content) {
      tbData.content.forEach(rec => {
        this.rowSelectOptions.push({ checked: false });
      });
    }
  }

  get tableData() {
    return this._tableData;
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

    this.clearSelection();

    this.onRefreshData.emit();
  }

  setPage(pageNum) {
    this.pagination.pageNum = pageNum;
    this.onRefreshData.emit();
  }

  selectAll() {
    
    this.rowSelectOptions.forEach(opt => opt.checked = true);

    if (this._tableData && this._tableData.content) {
      let selRecords = [];
      this._tableData.content.forEach(rec => selRecords.push(rec));
      this.selectedRecords = selRecords;
      this.availableActions = this.tableActionConfig.getAvailableActions(this.selectedRecords);
    }
  }

  selectNone() {
    this.rowSelectOptions.forEach(opt => opt.checked = false);
    this.selectedRecords = [];
    this.availableActions = this.tableActionConfig.getAvailableActions(this.selectedRecords);
  }

  onMultiSelectChange(value: string) {
    switch (value) {
      case "all":
        this.selectAll();
        break;
      
      case "none":
        this.selectNone();
        break;
    }
  }

  onRecordSelectChange($event: any, rec: any) {
    
    console.log(rec);

    if (this.tableActionConfig) {
    
      if ($event.target.checked) {
        // add if not present
        var indx = this.indexInSelectedRecords(rec);
        if (indx == -1) {
          this.selectedRecords.push(rec);
        }

      } else {
        // remove if present
        var indx = this.indexInSelectedRecords(rec);
        if (indx !== -1) {
          this.selectedRecords.splice(indx, 1);
        }
      }

      this.availableActions = this.tableActionConfig.getAvailableActions(this.selectedRecords);
    }
  }

  indexInSelectedRecords(rec: any) : number {
    for (var i = 0; i < this.selectedRecords.length; i++) {
      if (this.selectedRecords[i] === rec) {
        return i;
      }
    }
    return -1;
  }

  executeAction(act: TableAction<any>) {

    if (act) {

      act.execute(this.selectedRecords).subscribe(res => {

        this.clearSelection();

        if (act.successMessage) {
          act['tempMessage'] = act.successMessage;
          setTimeout(() => {
            act['tempMessage'] = null;
          }, 4000);
        }

      });
    }
  }

  clearSelection() {
    
    this.availableActions = [];
    this.selectedRecords = [];
    
    if (this.rowSelectOptions) {
      this.rowSelectOptions.forEach(opt => opt.checked = false);
    }

    if (this.tableActionConfig) {
      this.availableActions = this.tableActionConfig.getAvailableActions(this.selectedRecords);
    }
  }

}

class RowSelectOption {
  checked: boolean;
}