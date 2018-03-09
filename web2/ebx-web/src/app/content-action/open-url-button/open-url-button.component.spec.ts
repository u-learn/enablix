import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenUrlButtonComponent } from './open-url-button.component';

describe('OpenUrlButtonComponent', () => {
  let component: OpenUrlButtonComponent;
  let fixture: ComponentFixture<OpenUrlButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpenUrlButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenUrlButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
