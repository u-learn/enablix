import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddBizContentDialogButtonComponent } from './add-biz-content-dialog-button.component';

describe('AddBizContentDialogButtonComponent', () => {
  let component: AddBizContentDialogButtonComponent;
  let fixture: ComponentFixture<AddBizContentDialogButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddBizContentDialogButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddBizContentDialogButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
