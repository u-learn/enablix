import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EntityPillComponent } from './entity-pill.component';

describe('EntityPillComponent', () => {
  let component: EntityPillComponent;
  let fixture: ComponentFixture<EntityPillComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EntityPillComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EntityPillComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
