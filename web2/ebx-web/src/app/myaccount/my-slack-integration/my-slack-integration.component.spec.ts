import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MySlackIntegrationComponent } from './my-slack-integration.component';

describe('MySlackIntegrationComponent', () => {
  let component: MySlackIntegrationComponent;
  let fixture: ComponentFixture<MySlackIntegrationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MySlackIntegrationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MySlackIntegrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
