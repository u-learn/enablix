import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SlackSharePopupComponent } from './slack-share-popup.component';

describe('SlackSharePopupComponent', () => {
  let component: SlackSharePopupComponent;
  let fixture: ComponentFixture<SlackSharePopupComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SlackSharePopupComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SlackSharePopupComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
