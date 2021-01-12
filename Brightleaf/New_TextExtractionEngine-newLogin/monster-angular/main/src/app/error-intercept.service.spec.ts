import { TestBed } from '@angular/core/testing';

import { ErrorInterceptService } from './error-intercept.service';

describe('ErrorInterceptService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ErrorInterceptService = TestBed.get(ErrorInterceptService);
    expect(service).toBeTruthy();
  });
});
