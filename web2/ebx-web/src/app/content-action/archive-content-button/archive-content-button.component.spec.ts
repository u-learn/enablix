import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ArchiveContentButtonComponent } from './archive-content-button.component';

describe('ArchiveContentButtonComponent', () => {
  let component: ArchiveContentButtonComponent;
  let fixture: ComponentFixture<ArchiveContentButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ArchiveContentButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ArchiveContentButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
