import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RecentContentListComponent } from './recent-content-list.component';

describe('RecentContentListComponent', () => {
  let component: RecentContentListComponent;
  let fixture: ComponentFixture<RecentContentListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RecentContentListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RecentContentListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
