import { Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from '@angular/common';

@Pipe({
  name: 'ebxDateTimezone'
})
export class EbxDateTimezonePipe implements PipeTransform {

  dateFormat: string = "MMM d, y h:mm a";
  constructor(private datePipe: DatePipe) {}

  transform(value: any, args?: any): any {
    var rightNow = new Date();
    var timeZoneAbbr = String(String(rightNow).split("(")[1]).split(")")[0];
    return this.datePipe.transform(value, this.dateFormat) + " " + timeZoneAbbr;
  }

}
