import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { RuleexecutionComponent } from './ruleexecution.component';

describe('RuleexecutionComponent', () => {
  let component: RuleexecutionComponent;
  let fixture: ComponentFixture<RuleexecutionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ RuleexecutionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(RuleexecutionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
