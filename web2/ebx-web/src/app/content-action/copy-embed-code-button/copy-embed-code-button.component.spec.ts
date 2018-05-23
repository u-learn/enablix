import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CopyEmbedCodeButtonComponent } from './copy-embed-code-button.component';

describe('CopyEmbedCodeButtonComponent', () => {
  let component: CopyEmbedCodeButtonComponent;
  let fixture: ComponentFixture<CopyEmbedCodeButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CopyEmbedCodeButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CopyEmbedCodeButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
