import { TestBed, inject } from '@angular/core/testing';

import { ConfigInfoService } from './config-info.service';

describe('ConfigInfoService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ConfigInfoService]
    });
  });

  it('should be created', inject([ConfigInfoService], (service: ConfigInfoService) => {
    expect(service).toBeTruthy();
  }));
});
