import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentMappingLayoutComponent } from './content-mapping-layout.component';

describe('ContentMappingLayoutComponent', () => {
  let component: ContentMappingLayoutComponent;
  let fixture: ComponentFixture<ContentMappingLayoutComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentMappingLayoutComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentMappingLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
