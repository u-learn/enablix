import { TestBed, inject } from '@angular/core/testing';

import { RecoContentService } from './reco-content.service';

describe('RecoContentService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RecoContentService]
    });
  });

  it('should be created', inject([RecoContentService], (service: RecoContentService) => {
    expect(service).toBeTruthy();
  }));
});
