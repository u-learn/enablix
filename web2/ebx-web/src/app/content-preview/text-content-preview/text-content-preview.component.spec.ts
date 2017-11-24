import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TextContentPreviewComponent } from './text-content-preview.component';

describe('TextContentPreviewComponent', () => {
  let component: TextContentPreviewComponent;
  let fixture: ComponentFixture<TextContentPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TextContentPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TextContentPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
