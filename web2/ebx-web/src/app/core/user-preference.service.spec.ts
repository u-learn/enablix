import { TestBed, inject } from '@angular/core/testing';

import { UserPreferenceService } from './user-preference.service';

describe('UserPreferenceService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserPreferenceService]
    });
  });

  it('should be created', inject([UserPreferenceService], (service: UserPreferenceService) => {
    expect(service).toBeTruthy();
  }));
});
