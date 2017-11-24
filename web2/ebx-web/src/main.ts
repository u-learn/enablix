import { enableProdMode } from '@angular/core';
import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
import { environment } from './environments/environment';
import { ContentTemplate } from './app/model/content-template.model';

if (environment.production) {
  enableProdMode();
}

const contentTemplate = new ContentTemplate();

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.log(err));
