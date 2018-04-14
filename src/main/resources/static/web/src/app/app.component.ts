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
          this.messageService.set(e.error.key)
        })
  }

  withdraw(){
    this.accountService.withdraw(this.accountId, this.amount)
      .subscribe(resp =>{
          this.checkBalance()
        }, e => {
          console.log(e.error.key)
          this.messageService.set(e.error.key)
        })
  }

  accountIdChange(){
    this.checkBalance()
  }

  private checkBalance() {
    this.accountService.checkBalance(this.accountId)
    .subscribe(resp => {
          this.balance = resp.amount
          console.log(resp)
        }, e => {
          console.log(e.error.key)
          this.balance = null
          this.messageService.set(e.error.key)
        })
  }
}
