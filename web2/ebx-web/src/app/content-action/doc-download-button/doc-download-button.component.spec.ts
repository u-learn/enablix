import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocDownloadButtonComponent } from './doc-download-button.component';

describe('DocDownloadButtonComponent', () => {
  let component: DocDownloadButtonComponent;
  let fixture: ComponentFixture<DocDownloadButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DocDownloadButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocDownloadButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
