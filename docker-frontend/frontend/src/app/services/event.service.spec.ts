import { TestBed } from '@angular/core/testing';

import { ApplicationEventService } from './event.service';

describe('EventService', () => {
  let service: ApplicationEventService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApplicationEventService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
