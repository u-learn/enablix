import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CopyPortalUrlButtonComponent } from './copy-portal-url-button.component';

describe('CopyPortalUrlButtonComponent', () => {
  let component: CopyPortalUrlButtonComponent;
  let fixture: ComponentFixture<CopyPortalUrlButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CopyPortalUrlButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CopyPortalUrlButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
