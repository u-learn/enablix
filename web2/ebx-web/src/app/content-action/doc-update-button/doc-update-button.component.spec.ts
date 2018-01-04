import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocUpdateButtonComponent } from './doc-update-button.component';

describe('DocUpdateButtonComponent', () => {
  let component: DocUpdateButtonComponent;
  let fixture: ComponentFixture<DocUpdateButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DocUpdateButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocUpdateButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
