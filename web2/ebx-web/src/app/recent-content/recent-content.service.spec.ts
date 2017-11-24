import { TestBed, inject } from '@angular/core/testing';

import { RecentContentService } from './recent-content.service';

describe('RecentContentService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RecentContentService]
    });
  });

  it('should be created', inject([RecentContentService], (service: RecentContentService) => {
    expect(service).toBeTruthy();
  }));
});
