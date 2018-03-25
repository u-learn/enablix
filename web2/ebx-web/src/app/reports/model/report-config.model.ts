import { Observable } from 'rxjs/Observable';

import { FilterMetadata } from '../../core/data-search/filter-metadata.model';
import { DataFilter, DataFilterValueTx } from '../../core/data-filters/data-filters.model';

export class ReportConfig {
  id: string;
  name: string;
  heading: string;
  subheading: string;
  type: string;
  init: () => void;
  options: { [key: string]: any };
  chartOptions: { [key: string]: any };
  filterMetadata: { [key: string]: FilterMetadata};
  filters: DataFilter[];
  dataProvider: ReportDataProvider;
}

export interface ReportDataProvider {
  fetchData(filters: { [key: string]: any }) : Observable<any>;
  prepareChartData(reportData: any, filterValues: any) : any;
}

export class OffsetDaysFilterValueTx implements DataFilterValueTx {

  static theInstance: OffsetDaysFilterValueTx = new OffsetDaysFilterValueTx();

  transform(selectedValues: any) : any {
    
    if (selectedValues && selectedValues.length > 0) {
      return this.getOffsetDate(selectedValues[0].id);
    }
    
    return null;
  }

  getOffsetDate(offsetDays: number) : string {
            
    var m_names = new Array("Jan", "Feb", "Mar",
          "Apr", "May", "Jun", "Jul", "Aug", "Sep",
          "Oct", "Nov", "Dec");

    var d = new Date();
    var offsetTime = (24 * 60 * 60 * 1000) * offsetDays;
    d.setTime(d.getTime() - offsetTime);
    
    var curr_month = d.getMonth();
    var curr_date = d.getDate();
    var curr_year = d.getFullYear().toString().substr(2, 2);
    
    return curr_date + "-" + m_names[curr_month] + "-" + curr_year;
  }

}

