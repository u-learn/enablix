import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CpWidgetDetailComponent } from './cp-widget-detail.component';

describe('CpWidgetDetailComponent', () => {
  let component: CpWidgetDetailComponent;
  let fixture: ComponentFixture<CpWidgetDetailComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CpWidgetDetailComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CpWidgetDetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
