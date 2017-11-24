import { TestBed, inject } from '@angular/core/testing';

import { ContentPreviewService } from './content-preview.service';

describe('ContentPreviewService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ContentPreviewService]
    });
  });

  it('should be created', inject([ContentPreviewService], (service: ContentPreviewService) => {
    expect(service).toBeTruthy();
  }));
});
