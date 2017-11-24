import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FeaturedContentComponent } from './featured-content.component';

describe('FeaturedContentComponent', () => {
  let component: FeaturedContentComponent;
  let fixture: ComponentFixture<FeaturedContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FeaturedContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FeaturedContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
