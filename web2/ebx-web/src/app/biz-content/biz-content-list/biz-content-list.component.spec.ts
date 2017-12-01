import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BizContentListComponent } from './biz-content-list.component';

describe('BizContentListComponent', () => {
  let component: BizContentListComponent;
  let fixture: ComponentFixture<BizContentListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BizContentListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BizContentListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
