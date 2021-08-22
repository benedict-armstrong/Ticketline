import {TestBed} from '@angular/core/testing';

import {ApplicationPerformanceService} from './performance.service';

describe('ApplicationPerformanceService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ApplicationPerformanceService = TestBed.inject(ApplicationPerformanceService);
    expect(service).toBeTruthy();
  });
});
