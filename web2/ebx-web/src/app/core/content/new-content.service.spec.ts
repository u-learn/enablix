import { TestBed, inject } from '@angular/core/testing';

import { NewContentService } from './new-content.service';

describe('NewContentHolderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [NewContentService]
    });
  });

  it('should be created', inject([NewContentService], (service: NewContentService) => {
    expect(service).toBeTruthy();
  }));
});
