import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CopyDownloadUrlButtonComponent } from './copy-download-url-button.component';

describe('CopyDownloadUrlButtonComponent', () => {
  let component: CopyDownloadUrlButtonComponent;
  let fixture: ComponentFixture<CopyDownloadUrlButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CopyDownloadUrlButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CopyDownloadUrlButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
