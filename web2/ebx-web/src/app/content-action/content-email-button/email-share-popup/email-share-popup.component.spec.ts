import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmailSharePopupComponent } from './email-share-popup.component';

describe('EmailSharePopupComponent', () => {
  let component: EmailSharePopupComponent;
  let fixture: ComponentFixture<EmailSharePopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmailSharePopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmailSharePopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
