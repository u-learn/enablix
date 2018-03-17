import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecoContentComponent } from './reco-content.component';

describe('RecoContentComponent', () => {
  let component: RecoContentComponent;
  let fixture: ComponentFixture<RecoContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecoContentComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecoContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
