import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ContainerTemplateComponent } from './container-template.component';

describe('ContainerTemplateComponent', () => {
  let component: ContainerTemplateComponent;
  let fixture: ComponentFixture<ContainerTemplateComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ContainerTemplateComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ContainerTemplateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
