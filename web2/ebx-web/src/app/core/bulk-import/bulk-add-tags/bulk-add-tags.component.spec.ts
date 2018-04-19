import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BulkAddTagsComponent } from './bulk-add-tags.component';

describe('BulkAddTagsComponent', () => {
  let component: BulkAddTagsComponent;
  let fixture: ComponentFixture<BulkAddTagsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BulkAddTagsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BulkAddTagsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
