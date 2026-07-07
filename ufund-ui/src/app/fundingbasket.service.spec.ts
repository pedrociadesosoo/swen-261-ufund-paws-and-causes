import { TestBed } from '@angular/core/testing';

import { FundingbasketService } from './fundingbasket.service';

describe('FundingbasketService', () => {
  let service: FundingbasketService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(FundingbasketService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
