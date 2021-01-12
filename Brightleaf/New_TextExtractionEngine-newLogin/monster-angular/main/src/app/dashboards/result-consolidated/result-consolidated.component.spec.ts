import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ResultConsolidatedComponent } from './result-consolidated.component';

describe('ResultConsolidatedComponent', () => {
  let component: ResultConsolidatedComponent;
  let fixture: ComponentFixture<ResultConsolidatedComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ResultConsolidatedComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ResultConsolidatedComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
