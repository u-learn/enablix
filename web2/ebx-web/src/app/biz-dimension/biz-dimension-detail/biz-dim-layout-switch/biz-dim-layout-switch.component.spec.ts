import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BizDimLayoutSwitchComponent } from './biz-dim-layout-switch.component';

describe('BizDimLayoutSwitchComponent', () => {
  let component: BizDimLayoutSwitchComponent;
  let fixture: ComponentFixture<BizDimLayoutSwitchComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BizDimLayoutSwitchComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BizDimLayoutSwitchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
