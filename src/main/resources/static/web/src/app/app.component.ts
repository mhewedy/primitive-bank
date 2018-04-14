import { MessageService } from './shared/message/message.service';
import { AccountService } from './shared/account/account.service';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  
  title = 'Primitive Bank';
  accountId: number = 1;
  amount: number;
  balance: number;
  checkBalanceSucceed: boolean;

  constructor(private accountService: AccountService, private messageService: MessageService){
  }

  ngOnInit() {
    this.checkBalance()
  }

  deposit() {
    this.accountService.deposit(this.accountId, this.amount)
      .subscribe(resp =>{
          this.checkBalance()
        }, e => {
          console.log(e.error.key)
          this.messageService.set('server.' + e.error.key)
        })
  }

  withdraw(){
    this.accountService.withdraw(this.accountId, this.amount)
      .subscribe(resp =>{
          this.checkBalance()
        }, e => {
          console.log(e.error.key)
          this.messageService.set('server.' + e.error.key)
        })
  }

  accountIdChange(){
    if (this.accountId){
      this.checkBalance()
    }
  }

  private checkBalance() {
    this.messageService.clear();

    this.accountService.checkBalance(this.accountId)
    .subscribe(resp => {
          console.log(resp)
          this.balance = resp.amount
          this.checkBalanceSucceed = true
        }, e => {
          console.log(e.error.key)
          this.balance = null
          this.checkBalanceSucceed = false
          this.messageService.set('server.' + e.error.key)
        })
  }
}
