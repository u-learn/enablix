import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContentTypeCoverageComponent } from './content-type-coverage.component';

describe('ContentTypeCoverageComponent', () => {
  let component: ContentTypeCoverageComponent;
  let fixture: ComponentFixture<ContentTypeCoverageComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContentTypeCoverageComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContentTypeCoverageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
