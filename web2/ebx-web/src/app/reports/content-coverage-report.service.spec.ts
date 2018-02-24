import { TestBed, inject } from '@angular/core/testing';

import { ContentCoverageReportService } from './content-coverage-report.service';

describe('ContentCoverageReportService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ContentCoverageReportService]
    });
  });

  it('should be created', inject([ContentCoverageReportService], (service: ContentCoverageReportService) => {
    expect(service).toBeTruthy();
  }));
});
