import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SalesforceHomeComponent } from './salesforce-home.component';

describe('SalesforceHomeComponent', () => {
  let component: SalesforceHomeComponent;
  let fixture: ComponentFixture<SalesforceHomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ SalesforceHomeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SalesforceHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
