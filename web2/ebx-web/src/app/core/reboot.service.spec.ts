import { TestBed, inject } from '@angular/core/testing';

import { RebootService } from './reboot.service';

describe('RebootService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RebootService]
    });
  });

  it('should be created', inject([RebootService], (service: RebootService) => {
    expect(service).toBeTruthy();
  }));
});
