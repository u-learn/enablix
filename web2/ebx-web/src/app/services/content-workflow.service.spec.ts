import { TestBed, inject } from '@angular/core/testing';

import { ContentWorkflowService } from './content-workflow.service';

describe('ContentWorkflowService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ContentWorkflowService]
    });
  });

  it('should be created', inject([ContentWorkflowService], (service: ContentWorkflowService) => {
    expect(service).toBeTruthy();
  }));
});
