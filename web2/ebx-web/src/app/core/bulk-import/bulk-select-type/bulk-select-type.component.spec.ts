import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BulkSelectTypeComponent } from './bulk-select-type.component';

describe('BulkSelectTypeComponent', () => {
  let component: BulkSelectTypeComponent;
  let fixture: ComponentFixture<BulkSelectTypeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BulkSelectTypeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BulkSelectTypeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
