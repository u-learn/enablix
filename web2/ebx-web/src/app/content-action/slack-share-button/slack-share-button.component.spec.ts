import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SlackShareButtonComponent } from './slack-share-button.component';

describe('SlackShareButtonComponent', () => {
  let component: SlackShareButtonComponent;
  let fixture: ComponentFixture<SlackShareButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SlackShareButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SlackShareButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
