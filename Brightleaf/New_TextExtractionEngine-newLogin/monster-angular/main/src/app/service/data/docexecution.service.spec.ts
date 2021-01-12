import { TestBed } from '@angular/core/testing';

import { DocexecutionService } from './docexecution.service';

describe('DocexecutionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: DocexecutionService = TestBed.get(DocexecutionService);
    expect(service).toBeTruthy();
  });
});
