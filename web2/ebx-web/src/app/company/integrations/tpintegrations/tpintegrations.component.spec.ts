import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TPIntegrationsComponent } from './tpintegrations.component';

describe('TpintegrationsComponent', () => {
  let component: TPIntegrationsComponent;
  let fixture: ComponentFixture<TPIntegrationsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TPIntegrationsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TPIntegrationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
