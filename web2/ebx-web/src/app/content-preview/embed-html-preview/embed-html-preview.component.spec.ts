import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EmbedHtmlPreviewComponent } from './embed-html-preview.component';

describe('EmbedHtmlPreviewComponent', () => {
  let component: EmbedHtmlPreviewComponent;
  let fixture: ComponentFixture<EmbedHtmlPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EmbedHtmlPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EmbedHtmlPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
