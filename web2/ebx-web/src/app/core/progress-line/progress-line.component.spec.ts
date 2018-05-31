import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ProgressLineComponent } from './progress-line.component';

describe('ProgressLineComponent', () => {
  let component: ProgressLineComponent;
  let fixture: ComponentFixture<ProgressLineComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ProgressLineComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ProgressLineComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
