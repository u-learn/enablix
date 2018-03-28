import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecoListComponent } from './reco-list.component';

describe('RecoListComponent', () => {
  let component: RecoListComponent;
  let fixture: ComponentFixture<RecoListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecoListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecoListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
