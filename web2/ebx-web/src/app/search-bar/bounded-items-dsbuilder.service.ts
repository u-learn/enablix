import { Injectable } from '@angular/core';

import { Container } from '../model/container.model';
import { SearchDataset, NavFilter, ObjectType } from '../model/search-bar-data.model';
import { ContentTemplateService } from '../core/content-template.service';
import { Constants } from '../util/constants';
import { LocalDataset } from './local-dataset';

@Injectable()
export class BoundedItemsDSBuilderService {

  constructor(private ctService: ContentTemplateService) { }

  buildSearchDatasets(container: Container) : SearchDataset[] {
    
    let ds: SearchDataset[] = [];

    container.contentItem.forEach(ci => {

      if (ci.type == 'BOUNDED') {
        
        let ciDs = new LocalDataset(ci.label, []);
        ds.push(ciDs);

        this.ctService.getBoundedValueList(ci).subscribe(res => {
          
          let ciContainer = this.ctService.templateCache.getBoundedItemDatastoreContainer(ci);
          let color = Constants.refDataColor;

          if (ciContainer) {
            color = ciContainer.color;
          }

          let dsValues: NavFilter[] = ciDs.data;
          res.forEach(bv => {
            let nv = new NavFilter();
            nv.id = bv.id;
            nv.label = bv.label;
            nv.type = ObjectType.FILTER
            nv.color = color;
            nv.data = bv;

            dsValues.push(nv);
          });


        })
      }
    })

    return ds;
  }

}
