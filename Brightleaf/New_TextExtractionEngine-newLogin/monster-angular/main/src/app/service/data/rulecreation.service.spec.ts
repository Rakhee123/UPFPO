import { TestBed } from '@angular/core/testing';

import { RulecreationService } from './rulecreation.service';

describe('RulecreationService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: RulecreationService = TestBed.get(RulecreationService);
    expect(service).toBeTruthy();
  });
});
