import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BizDimensionListComponent } from './biz-dimension-list.component';

describe('BizDimensionListComponent', () => {
  let component: BizDimensionListComponent;
  let fixture: ComponentFixture<BizDimensionListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BizDimensionListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BizDimensionListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
