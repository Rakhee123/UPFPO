import { TestBed } from '@angular/core/testing';

import { CustomizelistService } from './customizelist.service';

describe('CustomizelistService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CustomizelistService = TestBed.get(CustomizelistService);
    expect(service).toBeTruthy();
  });
});
