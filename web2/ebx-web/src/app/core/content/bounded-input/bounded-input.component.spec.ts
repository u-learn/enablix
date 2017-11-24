import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BoundedInputComponent } from './bounded-input.component';

describe('BoundedInputComponent', () => {
  let component: BoundedInputComponent;
  let fixture: ComponentFixture<BoundedInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BoundedInputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BoundedInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
