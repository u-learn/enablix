import { TestBed, inject } from '@angular/core/testing';

import { ConsolidateContentService } from './consolidate-content.service';

describe('ConsolidateContentService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ConsolidateContentService]
    });
  });

  it('should be created', inject([ConsolidateContentService], (service: ConsolidateContentService) => {
    expect(service).toBeTruthy();
  }));
});
