import { TestBed } from '@angular/core/testing';

import { CheckPermissionService } from './check-permission.service';

describe('CheckPermissionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CheckPermissionService = TestBed.get(CheckPermissionService);
    expect(service).toBeTruthy();
  });
});
