import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AllDimensionsComponent } from './all-dimensions.component';

describe('AllDimensionsComponent', () => {
  let component: AllDimensionsComponent;
  let fixture: ComponentFixture<AllDimensionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AllDimensionsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AllDimensionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
