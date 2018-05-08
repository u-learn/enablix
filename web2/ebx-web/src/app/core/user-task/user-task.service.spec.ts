import { TestBed, inject } from '@angular/core/testing';

import { UserTaskService } from './user-task.service';

describe('UserTaskService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UserTaskService]
    });
  });

  it('should be created', inject([UserTaskService], (service: UserTaskService) => {
    expect(service).toBeTruthy();
  }));
});
