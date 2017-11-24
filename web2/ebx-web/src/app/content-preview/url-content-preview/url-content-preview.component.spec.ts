import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { UrlContentPreviewComponent } from './url-content-preview.component';

describe('UrlContentPreviewComponent', () => {
  let component: UrlContentPreviewComponent;
  let fixture: ComponentFixture<UrlContentPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ UrlContentPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(UrlContentPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
