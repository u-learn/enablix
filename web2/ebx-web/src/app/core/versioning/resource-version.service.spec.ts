import { TestBed, inject } from '@angular/core/testing';

import { ResourceVersionService } from './resource-version.service';

describe('ResourceVersionService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ResourceVersionService]
    });
  });

  it('should be created', inject([ResourceVersionService], (service: ResourceVersionService) => {
    expect(service).toBeTruthy();
  }));
});
