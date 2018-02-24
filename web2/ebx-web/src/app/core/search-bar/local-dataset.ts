import { Observable } from 'rxjs/Observable';

import { SearchDataset, SearchBarItem } from '../../model/search-bar-data.model';

export class LocalDataset implements SearchDataset {
  
  label: string;
  data: SearchBarItem[];

  constructor(lbl: string, dt: SearchBarItem[]) {
    this.data = dt;
    this.label = lbl;
  }

  getDatasetLabel() : string {
    return this.label;
  }

  getInitialData() : Observable<SearchBarItem[]> {
    return Observable.of(this.data);
  }

  getDataItems(filterText: string) : Observable<SearchBarItem[]> {
    return Observable.of(!filterText ? this.data : 
      this.data.filter(item => item.label.toLowerCase().indexOf(filterText.toLowerCase()) >= 0));
  }

}