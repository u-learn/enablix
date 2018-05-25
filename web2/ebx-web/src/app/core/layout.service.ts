import { Injectable } from '@angular/core';

import { UserPreferenceService } from './user-preference.service';

@Injectable()
export class LayoutService {

  static DIM_LAYOUTS_PREF_KEY = "biz.dim.layouts.config";

  dimLayoutConfigs: any;

  constructor(private userPrefs: UserPreferenceService) { 

  }

  getDimensionLayoutOptions(contentQId: string) {
    
    this.loadDimLayoutConfig();

    let options = [];
    for(var key in this.dimLayoutConfigs) {
      options.push(this.dimLayoutConfigs[key]);
    }
    return options;
  }

  loadDimLayoutConfig() {
    if (!this.dimLayoutConfigs) {
      var layoutPref = this.userPrefs.getPrefByKey(LayoutService.DIM_LAYOUTS_PREF_KEY);
      if (layoutPref) {
        this.dimLayoutConfigs = layoutPref.config;
      }
    }
  }

  getDimLayoutConfig(layoutId: string) {
    this.loadDimLayoutConfig();
    return this.dimLayoutConfigs ? this.dimLayoutConfigs[layoutId] : null;
  }

}
