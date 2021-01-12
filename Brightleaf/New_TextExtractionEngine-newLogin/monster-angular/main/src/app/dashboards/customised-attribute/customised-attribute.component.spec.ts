import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CustomisedAttributeComponent } from './customised-attribute.component';

describe('CustomisedAttributeComponent', () => {
  let component: CustomisedAttributeComponent;
  let fixture: ComponentFixture<CustomisedAttributeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CustomisedAttributeComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomisedAttributeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
