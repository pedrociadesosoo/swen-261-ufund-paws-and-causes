import { TestBed } from '@angular/core/testing';

import { Need } from './need';

describe('Need', () => {
  let service: Need;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Need);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
