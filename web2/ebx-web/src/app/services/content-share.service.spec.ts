import { TestBed, inject } from '@angular/core/testing';

import { ContentShareService } from './content-share.service';

describe('ContentShareService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ContentShareService]
    });
  });

  it('should be created', inject([ContentShareService], (service: ContentShareService) => {
    expect(service).toBeTruthy();
  }));
});
