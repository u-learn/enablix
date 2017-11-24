import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditBizDimensionComponent } from './edit-biz-dimension.component';

describe('EditBizDimensionComponent', () => {
  let component: EditBizDimensionComponent;
  let fixture: ComponentFixture<EditBizDimensionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditBizDimensionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditBizDimensionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
