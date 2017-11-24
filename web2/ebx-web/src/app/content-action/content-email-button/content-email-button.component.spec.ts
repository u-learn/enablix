import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentEmailButtonComponent } from './content-email-button.component';

describe('ContentEmailButtonComponent', () => {
  let component: ContentEmailButtonComponent;
  let fixture: ComponentFixture<ContentEmailButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentEmailButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentEmailButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
