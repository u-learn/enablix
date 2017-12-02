import { Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from '@angular/common';

@Pipe({
  name: 'ebxDate'
})
export class EbxDatePipe implements PipeTransform {

  dateFormat: string = "MMM d, y h:mm a";
  constructor(private datePipe: DatePipe) {}

  transform(value: any, args?: any): any {
    return this.datePipe.transform(value, this.dateFormat);
  }

}
