import { Amount } from './../amount.model';
import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from "rxjs/Observable";

@Injectable()
export class AccountService {

  constructor(private http: HttpClient) { }

  deposit(accountId: number, amount: number) {
    return this.http.put(`/api/v1/account/${accountId}/balance/deposit`, new Amount(amount));
  }

  withdraw(accountId: number, amount: number) {
    return this.http.put(`/api/v1/account/${accountId}/balance/withdraw`, new Amount(amount));
  }

  checkBalance(accountId: number): Observable<Amount> {
    return this.http.get<Amount>(`/api/v1/account/${accountId}/balance`);
  }
}
