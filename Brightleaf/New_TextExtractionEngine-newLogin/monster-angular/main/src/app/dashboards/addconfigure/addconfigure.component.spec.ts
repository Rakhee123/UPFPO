import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddconfigureComponent } from './addconfigure.component';

describe('AddconfigureComponent', () => {
  let component: AddconfigureComponent;
  let fixture: ComponentFixture<AddconfigureComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddconfigureComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddconfigureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
