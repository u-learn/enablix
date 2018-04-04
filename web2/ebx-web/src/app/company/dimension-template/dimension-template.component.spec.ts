import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DimensionTemplateComponent } from './dimension-template.component';

describe('DimensionTemplateComponent', () => {
  let component: DimensionTemplateComponent;
  let fixture: ComponentFixture<DimensionTemplateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DimensionTemplateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DimensionTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
