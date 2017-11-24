import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddContentTagsComponent } from './add-content-tags.component';

describe('AddContentTagsComponent', () => {
  let component: AddContentTagsComponent;
  let fixture: ComponentFixture<AddContentTagsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddContentTagsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddContentTagsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
