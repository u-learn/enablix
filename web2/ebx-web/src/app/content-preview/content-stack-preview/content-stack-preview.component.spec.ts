import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentStackPreviewComponent } from './content-stack-preview.component';

describe('ContentStackPreviewComponent', () => {
  let component: ContentStackPreviewComponent;
  let fixture: ComponentFixture<ContentStackPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentStackPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentStackPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
