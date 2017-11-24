import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BizContentComponent } from './biz-content.component';

describe('BizContentComponent', () => {
  let component: BizContentComponent;
  let fixture: ComponentFixture<BizContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BizContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BizContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
