import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentReqRejectButtonComponent } from './content-req-reject-button.component';

describe('ContentReqRejectButtonComponent', () => {
  let component: ContentReqRejectButtonComponent;
  let fixture: ComponentFixture<ContentReqRejectButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentReqRejectButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentReqRejectButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
