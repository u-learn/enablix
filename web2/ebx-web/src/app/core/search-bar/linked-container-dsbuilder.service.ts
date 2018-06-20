import { Injectable } from '@angular/core';

import { Container } from '../../model/container.model';
import { SearchDataset, NavFilter, ObjectType, NavContext, NavCtxItem, SearchBarData } from '../../model/search-bar-data.model';
import { ContentTemplateService } from '../content-template.service';
import { Constants } from '../../util/constants';
import { LocalDataset } from './local-dataset';

@Injectable()
export class LinkedContainerDsbuilderService {

  constructor(private ctService: ContentTemplateService) { }

  buildSearchDatasets(container: Container, sbData: SearchBarData) : SearchDataset[] {
    
    let ds: SearchDataset[] = [];

    // search bar row of business content
    let bizContentSearchItems: NavCtxItem[] = [];
    let bizDimSearchItems: NavCtxItem[] = [];
    
    container.container.forEach(cont => {
        
      var cc = this.ctService.getConcreteContainerByQId(cont.qualifiedId);
      
      if (cc) {
        let item = new NavCtxItem();
        
        item.id = cc.qualifiedId;
        item.label = cc.label;
        item.type = ObjectType.BIZ_CONTENT;
        item.color = cc.color;
        item.data = cc;
        
        item.routeParams = [cc.qualifiedId];

        if (this.ctService.isBusinessContent(cc)) {
          bizContentSearchItems.push(item);  
        } else if (this.ctService.isBusinessDimension(cc)) {
          bizDimSearchItems.push(item);
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
