import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BizDimensionDetailComponent } from './biz-dimension-detail.component';

describe('BizDimensionDetailComponent', () => {
  let component: BizDimensionDetailComponent;
  let fixture: ComponentFixture<BizDimensionDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BizDimensionDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BizDimensionDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
