import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MyContentRequestComponent } from './my-content-request.component';

describe('MyContentRequestComponent', () => {
  let component: MyContentRequestComponent;
  let fixture: ComponentFixture<MyContentRequestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MyContentRequestComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MyContentRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
