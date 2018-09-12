import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentEngagementDistributionComponent } from './content-engagement-distribution.component';

describe('ContentEngagementDistributionComponent', () => {
  let component: ContentEngagementDistributionComponent;
  let fixture: ComponentFixture<ContentEngagementDistributionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentEngagementDistributionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentEngagementDistributionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
