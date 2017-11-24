import { TestBed, inject } from '@angular/core/testing';

import { DataSearchService } from './data-search.service';

describe('DataSearchService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DataSearchService]
    });
  });

  it('should be created', inject([DataSearchService], (service: DataSearchService) => {
    expect(service).toBeTruthy();
  }));
});
