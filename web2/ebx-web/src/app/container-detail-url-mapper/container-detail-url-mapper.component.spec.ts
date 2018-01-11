import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerDetailUrlMapperComponent } from './container-detail-url-mapper.component';

describe('ContainerDetailUrlMapperComponent', () => {
  let component: ContainerDetailUrlMapperComponent;
  let fixture: ComponentFixture<ContainerDetailUrlMapperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContainerDetailUrlMapperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerDetailUrlMapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
