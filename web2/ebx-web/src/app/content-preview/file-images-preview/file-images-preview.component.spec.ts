import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FileImagesPreviewComponent } from './file-images-preview.component';

describe('FileImagesPreviewComponent', () => {
  let component: FileImagesPreviewComponent;
  let fixture: ComponentFixture<FileImagesPreviewComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FileImagesPreviewComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FileImagesPreviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
