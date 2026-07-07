import { TestBed } from '@angular/core/testing';

import { AccountService } from './account';

describe('AccountService', () => {
  let service: AccountService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should start with no one logged in', () => {
    expect(service.isLoggedIn()).toBeFalse();
    expect(service.getCurrentAccount()).toBeNull();
  });
});
