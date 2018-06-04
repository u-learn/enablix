import { TestBed, inject } from '@angular/core/testing';

import { UiWidgetService } from './ui-widget.service';

describe('UiWidgetService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UiWidgetService]
    });
  });

  it('should be created', inject([UiWidgetService], (service: UiWidgetService) => {
    expect(service).toBeTruthy();
  }));
});
