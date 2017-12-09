import { TestBed, inject } from '@angular/core/testing';

import { FreetextSearchService } from './freetext-search.service';

describe('FreetextSearchService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [FreetextSearchService]
    });
  });

  it('should be created', inject([FreetextSearchService], (service: FreetextSearchService) => {
    expect(service).toBeTruthy();
  }));
});
