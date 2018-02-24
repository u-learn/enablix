import { TestBed, inject } from '@angular/core/testing';

import { ActivitySummaryReportService } from './activity-summary-report.service';

describe('ActivitySummaryReportService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ActivitySummaryReportService]
    });
  });

  it('should be created', inject([ActivitySummaryReportService], (service: ActivitySummaryReportService) => {
    expect(service).toBeTruthy();
  }));
});
