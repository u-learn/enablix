import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AppUrlMapperComponent } from './app-url-mapper.component';

describe('AppUrlMapperComponent', () => {
  let component: AppUrlMapperComponent;
  let fixture: ComponentFixture<AppUrlMapperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppUrlMapperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppUrlMapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
