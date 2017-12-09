import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FreetextSearchComponent } from './freetext-search.component';

describe('FreetextSearchComponent', () => {
  let component: FreetextSearchComponent;
  let fixture: ComponentFixture<FreetextSearchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FreetextSearchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FreetextSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
