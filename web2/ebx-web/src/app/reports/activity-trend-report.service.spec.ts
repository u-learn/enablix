import { TestBed, inject } from '@angular/core/testing';

import { ActivityTrendReportService } from './activity-trend-report.service';

describe('ActivityTrendReportService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ActivityTrendReportService]
    });
  });

  it('should be created', inject([ActivityTrendReportService], (service: ActivityTrendReportService) => {
    expect(service).toBeTruthy();
  }));
});
