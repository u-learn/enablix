import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OppAttributionComponent } from './opp-attribution.component';

describe('OppAttributionComponent', () => {
  let component: OppAttributionComponent;
  let fixture: ComponentFixture<OppAttributionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OppAttributionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OppAttributionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
