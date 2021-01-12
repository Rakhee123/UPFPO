import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddruletorulesetComponent } from './addruletoruleset.component';

describe('AddruletorulesetComponent', () => {
  let component: AddruletorulesetComponent;
  let fixture: ComponentFixture<AddruletorulesetComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddruletorulesetComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddruletorulesetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
