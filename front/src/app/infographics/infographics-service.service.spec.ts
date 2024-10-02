import { TestBed } from '@angular/core/testing';

import { InfographicsServiceService } from './infographics-service.service';

describe('InfographicsServiceService', () => {
  let service: InfographicsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InfographicsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
