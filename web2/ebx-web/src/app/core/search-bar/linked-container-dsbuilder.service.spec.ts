import { TestBed, inject } from '@angular/core/testing';

import { LinkedContainerDsbuilderService } from './linked-container-dsbuilder.service';

describe('LinkedContainerDsbuilderService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [LinkedContainerDsbuilderService]
    });
  });

  it('should be created', inject([LinkedContainerDsbuilderService], (service: LinkedContainerDsbuilderService) => {
    expect(service).toBeTruthy();
  }));
});
