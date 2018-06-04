import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentPackWidgetComponent } from './content-pack-widget.component';

describe('ContentPackWidgetComponent', () => {
  let component: ContentPackWidgetComponent;
  let fixture: ComponentFixture<ContentPackWidgetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentPackWidgetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentPackWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
