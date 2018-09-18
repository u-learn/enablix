import { Component, OnInit } from '@angular/core';

import { AppContext } from './app-context';
import { Utility } from './util/utility';

@Component({
  selector: 'ebx-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
  title = 'app';
  appCtx = AppContext;

  ngOnInit() {

    let ctxEmbedded: string = Utility.getQueryString("ctx_embedded");
    if (ctxEmbedded != null) {
      AppContext.embedded = Utility.stringToBoolean(ctxEmbedded);
    }

    let ctxFullpage: string = Utility.getQueryString("ctx_fullPage");
    if (ctxFullpage != null) {
      AppContext.fullPage = Utility.stringToBoolean(ctxFullpage);
    }

    let ctxHomepage: string = Utility.getQueryString("ctx_homePage");
    if (ctxHomepage) {
      AppContext.homePage = ctxHomepage;
    }

    let ctxChannel: string = Utility.getQueryString("ctx_channel");
    if (ctxChannel) {
      AppContext.channel = ctxChannel;
    }
    
  }

}
