import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentDeleteButtonComponent } from './content-delete-button.component';

describe('ContentDeleteButtonComponent', () => {
  let component: ContentDeleteButtonComponent;
  let fixture: ComponentFixture<ContentDeleteButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentDeleteButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentDeleteButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
