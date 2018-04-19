import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GdriveImportComponent } from './gdrive-import.component';

describe('GdriveImportComponent', () => {
  let component: GdriveImportComponent;
  let fixture: ComponentFixture<GdriveImportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GdriveImportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GdriveImportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
