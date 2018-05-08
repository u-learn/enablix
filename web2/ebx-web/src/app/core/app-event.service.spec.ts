import { TestBed, inject } from '@angular/core/testing';

import { AppEventService } from './app-event.service';

describe('AppEventService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AppEventService]
    });
  });

  it('should be created', inject([AppEventService], (service: AppEventService) => {
    expect(service).toBeTruthy();
  }));
});
