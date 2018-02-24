import { Injectable } from '@angular/core';

import { Container } from '../../model/container.model';
import { SearchDataset, NavFilter, ObjectType } from '../../model/search-bar-data.model';
import { ContentTemplateService } from '../content-template.service';
import { Constants } from '../../util/constants';
import { LocalDataset } from './local-dataset';
import { SearchBarData } from '../../model/search-bar-data.model';

@Injectable()
export class BoundedItemsDSBuilderService {

  constructor(private ctService: ContentTemplateService) { }

  buildSearchDatasets(container: Container, sbData: SearchBarData) : SearchDataset[] {
    
    let ds: SearchDataset[] = [];

    container.contentItem.forEach(ci => {

      if (ci.type == 'BOUNDED') {
        
        let ciDs = new LocalDataset(ci.label, []);
        ds.push(ciDs);

        this.ctService.getBoundedValueList(ci).subscribe(res => {
          
          let ciContainer = this.ctService.templateCache.getBoundedItemDatastoreContainer(ci);
          let queryParamName = null;          
          let color = Constants.refDataColor;

          if (ciContainer) {
            color = ciContainer.color;
            queryParamName = "sf_" + ci.id;
          }

          let dsValues: NavFilter[] = ciDs.data;
          res.forEach(bv => {
            let nv = new NavFilter();
            nv.id = bv.id;
            nv.label = bv.label;
            nv.type = ObjectType.FILTER
            nv.color = color;
            nv.data = bv;

            if (queryParamName) {
              nv.queryParams[queryParamName] = bv.id;  
            }

            if (sbData.initialFilterIds.indexOf(bv.id) >= 0) {
              sbData.filters.push(nv);
            }

            dsValues.push(nv);
          });


        })
      }
    })

    return ds;
  }

}
