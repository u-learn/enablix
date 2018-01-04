import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentReqApproveButtonComponent } from './content-req-approve-button.component';

describe('ContentReqApproveButtonComponent', () => {
  let component: ContentReqApproveButtonComponent;
  let fixture: ComponentFixture<ContentReqApproveButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentReqApproveButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentReqApproveButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
