import { TestBed, inject } from '@angular/core/testing';

import { GlobalSearchControllerService } from './global-search-controller.service';

describe('GlobalSearchControllerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GlobalSearchControllerService]
    });
  });

  it('should be created', inject([GlobalSearchControllerService], (service: GlobalSearchControllerService) => {
    expect(service).toBeTruthy();
  }));
});
