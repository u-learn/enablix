import { TestBed, inject } from '@angular/core/testing';

import { BoundedItemsDSBuilderService } from './bounded-items-dsbuilder.service';

describe('BoundedItemsDSBuilderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BoundedItemsDSBuilderService]
    });
  });

  it('should be created', inject([BoundedItemsDSBuilderService], (service: BoundedItemsDSBuilderService) => {
    expect(service).toBeTruthy();
  }));
});
