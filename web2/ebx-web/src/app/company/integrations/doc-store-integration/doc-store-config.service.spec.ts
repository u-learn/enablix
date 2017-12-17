import { TestBed, inject } from '@angular/core/testing';

import { DocStoreConfigService } from './doc-store-config.service';

describe('DocStoreConfigService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DocStoreConfigService]
    });
  });

  it('should be created', inject([DocStoreConfigService], (service: DocStoreConfigService) => {
    expect(service).toBeTruthy();
  }));
});
