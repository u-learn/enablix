import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ObsoleteContentComponent } from './obsolete-content.component';

describe('ObsoleteContentComponent', () => {
  let component: ObsoleteContentComponent;
  let fixture: ComponentFixture<ObsoleteContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ObsoleteContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ObsoleteContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
