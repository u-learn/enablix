import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditCompanyPropValueComponent } from './edit-company-prop-value.component';

describe('EditCompanyPropValueComponent', () => {
  let component: EditCompanyPropValueComponent;
  let fixture: ComponentFixture<EditCompanyPropValueComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditCompanyPropValueComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditCompanyPropValueComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
