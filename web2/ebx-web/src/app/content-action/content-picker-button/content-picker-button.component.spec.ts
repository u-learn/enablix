import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentPickerButtonComponent } from './content-picker-button.component';

describe('ContentPickerButtonComponent', () => {
  let component: ContentPickerButtonComponent;
  let fixture: ComponentFixture<ContentPickerButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentPickerButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentPickerButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
