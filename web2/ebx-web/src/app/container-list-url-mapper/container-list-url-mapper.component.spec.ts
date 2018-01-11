import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerListUrlMapperComponent } from './container-list-url-mapper.component';

describe('ContainerListUrlMapperComponent', () => {
  let component: ContainerListUrlMapperComponent;
  let fixture: ComponentFixture<ContainerListUrlMapperComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContainerListUrlMapperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerListUrlMapperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
