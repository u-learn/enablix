import { TestBed, inject } from '@angular/core/testing';

import { ActivityAuditService } from './activity-audit.service';

describe('ActivityAuditService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ActivityAuditService]
    });
  });

  it('should be created', inject([ActivityAuditService], (service: ActivityAuditService) => {
    expect(service).toBeTruthy();
  }));
});
