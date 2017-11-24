import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BizDimensionComponent } from './biz-dimension.component';

describe('BizDimensionComponent', () => {
  let component: BizDimensionComponent;
  let fixture: ComponentFixture<BizDimensionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BizDimensionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BizDimensionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
