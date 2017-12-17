import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { DocStoreIntegrationComponent } from './doc-store-integration.component';

describe('DocStoreIntegrationComponent', () => {
  let component: DocStoreIntegrationComponent;
  let fixture: ComponentFixture<DocStoreIntegrationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ DocStoreIntegrationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(DocStoreIntegrationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
