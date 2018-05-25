import { TestBed, inject } from '@angular/core/testing';

import { ContentConnService } from './content-conn.service';

describe('ContentConnService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ContentConnService]
    });
  });

  it('should be created', inject([ContentConnService], (service: ContentConnService) => {
    expect(service).toBeTruthy();
  }));
});
