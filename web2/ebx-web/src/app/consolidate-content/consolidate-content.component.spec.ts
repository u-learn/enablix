import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsolidateContentComponent } from './consolidate-content.component';

describe('ConsolidateContentComponent', () => {
  let component: ConsolidateContentComponent;
  let fixture: ComponentFixture<ConsolidateContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ConsolidateContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsolidateContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
