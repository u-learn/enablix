import { Observable } from 'rxjs/Observable';

import { Container } from './container.model';

export class SearchBarData {
  
  context: NavContext;
  filters: NavFilter[] = [];
  datasets: SearchDataset[] = [];

  addFilter(filter: NavFilter) {
    return this.addSearchBarItem(this.filters, filter);
  }

  addContextItem(ctxItem: NavCtxItem) {

    if (this.context) {
      return this.addSearchBarItem(this.context.contextItems, ctxItem);
    }

    return false;
  }

  removeFilter(filter: NavFilter) {
    return this.removeSearchBarItem(this.filters, filter);
  }

  removeContextItem(ctxItem: NavCtxItem) {

    if (this.context) {
      return this.removeSearchBarItem(this.context.contextItems, ctxItem);
    }

    return false;
  }

  addSearchBarItem(holderArr: SearchBarItem[], item: SearchBarItem) {

    if (this.getItemIndex(holderArr, item) == -1) {
      holderArr.push(item);
      return true;
    }

    return false;
  }

  getItemIndex(items: SearchBarItem[], item: SearchBarItem) {
    for (let i = 0; i < items.length; i++) {
      if (item.id === items[i].id) {
        return i;
      }
    }
    return -1;
  }

  removeSearchBarItem(holderArr: SearchBarItem[], item: SearchBarItem) {

    let indx = this.getItemIndex(holderArr, item);
    
    if (indx != -1) {
      holderArr.splice(indx, 1);
      return true;
    }

    return false;
  }

}

export abstract class SearchBarItem {
  
  id: string;
  label: string;
  type: ObjectType;
  color: string;

  data?: any;

  abstract addToSearchBarData(sbData: SearchBarData) : void;
  abstract removeFromSearchBarData(sbData: SearchBarData) : void;

}

export class NavCtxItem extends SearchBarItem {

  routeParams: string[] = [];

  addToSearchBarData(sbData: SearchBarData) {
    sbData.addContextItem(this)
  }

  removeFromSearchBarData(sbData: SearchBarData) {
    sbData.removeContextItem(this)
  }
}

export class NavFilter extends SearchBarItem {
  
  addToSearchBarData(sbData: SearchBarData) {
    sbData.addFilter(this);
  }

  removeFromSearchBarData(sbData: SearchBarData) {
    sbData.removeFilter(this);
  }
}

export interface SearchDataset {
  getDatasetLabel() : string;
  getDataItems(filterText: string) : Observable<SearchBarItem[]>;
} 

export class NavContext {
  contextItems: NavCtxItem[] = [];
}


export enum ObjectType {
  
  BIZ_CONTENT = "BIZ_CONTENT",
  BIZ_DIM = "BIZ_DIM",
  BIZ_CONTENT_OBJ = "BIZ_CONTENT_OBJ", 
  BIZ_DIM_OBJ = "BIZ_DIM_OBJ",

  FILTER = "FILTER"
}

