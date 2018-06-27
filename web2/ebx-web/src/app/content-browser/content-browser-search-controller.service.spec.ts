import { TestBed, inject } from '@angular/core/testing';

import { ContentBrowserSearchControllerService } from './content-browser-search-controller.service';

describe('ContentBrowserSearchControllerService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ContentBrowserSearchControllerService]
    });
  });

  it('should be created', inject([ContentBrowserSearchControllerService], (service: ContentBrowserSearchControllerService) => {
    expect(service).toBeTruthy();
  }));
});
