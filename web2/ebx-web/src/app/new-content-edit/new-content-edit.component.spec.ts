import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NewContentEditComponent } from './new-content-edit.component';

describe('NewContentEditComponent', () => {
  let component: NewContentEditComponent;
  let fixture: ComponentFixture<NewContentEditComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NewContentEditComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewContentEditComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
