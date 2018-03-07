import { TestBed, inject } from '@angular/core/testing';

import { AppMessageService } from './app-message.service';

describe('AppMessageService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AppMessageService]
    });
  });

  it('should be created', inject([AppMessageService], (service: AppMessageService) => {
    expect(service).toBeTruthy();
  }));
});
