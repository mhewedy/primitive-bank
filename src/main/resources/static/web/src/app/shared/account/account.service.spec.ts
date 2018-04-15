import {TestBed, inject} from '@angular/core/testing';
import {AccountService} from './account.service';
import {HttpClient, HttpClientModule} from '@angular/common/http';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {Amount} from "../amount.model";


describe('AccountService', () => {

  let service: AccountService;
  let httpMock;


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [HttpClientModule, AccountService]
    });

    service = TestBed.get(AccountService);
    httpMock = TestBed.get(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });


  it('should be created', inject([AccountService], (accountService: AccountService) => {
    expect(accountService).toBeTruthy();
  }));

  it('should call correct URL/method of check balance', () => {
    service.checkBalance(101).subscribe(() => {
    });

    const req = httpMock.expectOne({method: 'GET'});
    const resourceUrl = '/api/v1/account/101/balance';
    expect(req.request.url).toEqual(resourceUrl);
  });

  it('should call correct URL/method of deposit', () => {
    service.deposit(101, 300.00).subscribe(() => {
    });

    const req = httpMock.expectOne({method: 'PUT'});
    const resourceUrl = '/api/v1/account/101/balance/deposit';
    expect(req.request.url).toEqual(resourceUrl);
    expect(req.request.body).toEqual(new Amount(300.00))
  });

  it('should call correct URL/method of withdraw', () => {
    service.withdraw(101, 300.00).subscribe(() => {
    });

    const req = httpMock.expectOne({method: 'PUT'});
    const resourceUrl = '/api/v1/account/101/balance/withdraw';
    expect(req.request.url).toEqual(resourceUrl);
    expect(req.request.body).toEqual(new Amount(300.00))
  });

  it('should propagate not found response', () => {
    service.checkBalance(101).subscribe(null, (_error: any) => {
      expect(_error.status).toEqual(404);
    });

    const req = httpMock.expectOne({method: 'GET'});
    req.flush('Invalid request parameters', {
      status: 404,
      statusText: 'Bad Request'
    });
  });

});
