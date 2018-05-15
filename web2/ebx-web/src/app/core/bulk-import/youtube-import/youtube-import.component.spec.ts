import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { YoutubeImportComponent } from './youtube-import.component';

describe('YoutubeImportComponent', () => {
  let component: YoutubeImportComponent;
  let fixture: ComponentFixture<YoutubeImportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ YoutubeImportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(YoutubeImportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
