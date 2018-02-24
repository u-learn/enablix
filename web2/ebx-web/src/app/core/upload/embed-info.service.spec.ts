import { TestBed, inject } from '@angular/core/testing';

import { EmbedInfoService } from './embed-info.service';

describe('EmbedInfoService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [EmbedInfoService]
    });
  });

  it('should be created', inject([EmbedInfoService], (service: EmbedInfoService) => {
    expect(service).toBeTruthy();
  }));
});
