import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentEngagementDemoComponent } from './content-engagement-demo.component';

describe('ContentEngagementDemoComponent', () => {
  let component: ContentEngagementDemoComponent;
  let fixture: ComponentFixture<ContentEngagementDemoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentEngagementDemoComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentEngagementDemoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
