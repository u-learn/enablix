import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentStackInputComponent } from './content-stack-input.component';

describe('ContentStackInputComponent', () => {
  let component: ContentStackInputComponent;
  let fixture: ComponentFixture<ContentStackInputComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentStackInputComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentStackInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
