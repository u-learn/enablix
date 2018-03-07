import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppIntroMsgComponent } from './app-intro-msg.component';

describe('AppIntroMsgComponent', () => {
  let component: AppIntroMsgComponent;
  let fixture: ComponentFixture<AppIntroMsgComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppIntroMsgComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppIntroMsgComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
