import { TestBed, inject } from '@angular/core/testing';

import { ContentQualityService } from './content-quality.service';

describe('ContentQualityService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ContentQualityService]
    });
  });

  it('should be created', inject([ContentQualityService], (service: ContentQualityService) => {
    expect(service).toBeTruthy();
  }));
});
