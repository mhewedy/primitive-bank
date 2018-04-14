import { TestBed, inject } from '@angular/core/testing';

import { AccountService } from './account.service';
import { HttpClient, HttpClientModule } from '@angular/common/http';
//https://medium.com/netscape/testing-with-the-angular-httpclient-api-648203820712
describe('AccountService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [HttpClientModule, AccountService]
    });
  });

  /*it('should be created', inject([AccountService], (service: AccountService) => {
    expect(service).toBeTruthy();
  }));
  */
});
