import { Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from '@angular/common';

@Pipe({
  name: 'ebxDateOnly'
})
export class EbxDateOnlyPipe implements PipeTransform {

  dateFormat: string = "MMM d, y";
  constructor(private datePipe: DatePipe) {}

  transform(value: any, args?: any): any {
    return this.datePipe.transform(value, this.dateFormat);
  }

}
