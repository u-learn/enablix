import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ThumbnailUpdateButtonComponent } from './thumbnail-update-button.component';

describe('ThumbnailUpdateButtonComponent', () => {
  let component: ThumbnailUpdateButtonComponent;
  let fixture: ComponentFixture<ThumbnailUpdateButtonComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ThumbnailUpdateButtonComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ThumbnailUpdateButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
