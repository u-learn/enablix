import { Pipe, PipeTransform } from '@angular/core';

import { ApiUrlService } from '../api-url.service';

@Pipe({
  name: 'ebxLinkify'
})
export class EbxLinkifyPipe implements PipeTransform {

  linkyUrlRegex: RegExp = /((ftp|https?):\/\/|(www\.))([^\s;,(){}<>"”’]*)/g;

  constructor(private apiUrlService: ApiUrlService) { }

  transform(value: any, args?: any): any {
    
    if (!value) return value;

    var linkParams = args ? args.linkParams : null;
    const target = args ? args.target : "_blank";

    return value.replace(this.linkyUrlRegex, (match) => {
      return this.convertToLink(match, target, linkParams);
    });

  }

  convertToLink(url, target, linkParams) {
  
    var html: string[] = [];
    var xlinkUrl = linkParams && linkParams.cQId && linkParams.cId ? 
      this.apiUrlService.getExtLinkUrl(url, linkParams.cId, linkParams.cQId) : url;

    html.push('<a class="xlink"');
    html.push('target="', target, '" ');
    html.push('href="', xlinkUrl.replace(/"/g, '&quot;'), '">');
    html.push("Click Here");
    html.push('</a>');

    return html.join('');
  }
}
