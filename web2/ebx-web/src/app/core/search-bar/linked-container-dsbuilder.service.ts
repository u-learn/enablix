import { Injectable } from '@angular/core';

import { Container } from '../../model/container.model';
import { SearchDataset, NavFilter, ObjectType, NavContext, NavCtxItem, SearchBarData } from '../../model/search-bar-data.model';
import { ContentTemplateService } from '../content-template.service';
import { Constants } from '../../util/constants';
import { LocalDataset } from './local-dataset';

@Injectable()
export class LinkedContainerDsbuilderService {

  constructor(private ctService: ContentTemplateService) { }

  buildSearchDatasets(container: Container, childQIds: any, sbData: SearchBarData) : SearchDataset[] {
    
    let ds: SearchDataset[] = [];

    // search bar row of business content
    let bizContentSearchItems: NavFilter[] = [];
    let bizDimSearchItems: NavFilter[] = [];
    
    container.container.forEach(cont => {
        
      if (childQIds && childQIds.indexOf(cont.qualifiedId) < 0) {
        return;
      }
      
      var cc = this.ctService.getConcreteContainerByQId(cont.qualifiedId);
      
      if (cc) {
        let item = new NavFilter();
        
        item.id = cc.qualifiedId;
        item.label = cc.label;
        item.type = ObjectType.BIZ_CONTENT;
        item.color = cc.color;
        item.data = cc;
        
        item.queryParams = { "sf_cqid" : cc.qualifiedId };

        if (this.ctService.isBusinessContent(cc)) {
          bizContentSearchItems.push(item);  
        } else if (this.ctService.isBusinessDimension(cc)) {
          bizDimSearchItems.push(item);
        }

        if (sbData.initialFilterIds.indexOf(item.id) >= 0) {
          sbData.filters.push(item);
        }
        
      }

    });

    let bizContentListDataset = new LocalDataset("Content Types", bizContentSearchItems);
    ds.push(bizContentListDataset);

    let bizDimListDataset = new LocalDataset("Objects", bizDimSearchItems);
    ds.push(bizDimListDataset);

    return ds;
  }
}
