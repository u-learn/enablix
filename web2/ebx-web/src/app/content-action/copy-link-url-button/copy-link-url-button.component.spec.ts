import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CopyLinkUrlButtonComponent } from './copy-link-url-button.component';

describe('CopyLinkUrlButtonComponent', () => {
  let component: CopyLinkUrlButtonComponent;
  let fixture: ComponentFixture<CopyLinkUrlButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CopyLinkUrlButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CopyLinkUrlButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
