import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'ebx-pagination',
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class PaginationComponent implements OnInit {

  @Input() totalPages: number;
  @Input() currentPage: number;

  @Output() onPageChange = new EventEmitter<number>();

  showingPages: number;

  constructor() { }

  ngOnInit() {

  }

  getPageNumbers() {
        
    var firstPageNum = 0;
    var lastPageNum = this.totalPages - 1;
    
    if (this.totalPages >= 10) {

      firstPageNum = this.currentPage - 5;
      if (firstPageNum < 1) {
        firstPageNum = 0;
      }
      
      lastPageNum = firstPageNum + 9;
      if (lastPageNum >= this.totalPages) {
        lastPageNum = this.totalPages - 1;
      }

    }
    
    var ret = [];
    for (var i = firstPageNum; i <= lastPageNum; i++) {
      ret.push(i);
    }

    this.showingPages = ret.length;
    
    return ret;
  }
    
  setPage(pageNum: number) {
    if (pageNum >=0 && pageNum < this.totalPages && pageNum != this.currentPage) {
      this.onPageChange.emit(pageNum);
    }
  }

}
