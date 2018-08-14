import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CompanyPropertyComponent } from './company-property.component';

describe('CompanyPropertyComponent', () => {
  let component: CompanyPropertyComponent;
  let fixture: ComponentFixture<CompanyPropertyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CompanyPropertyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CompanyPropertyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
