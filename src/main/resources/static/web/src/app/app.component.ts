import {MessageService} from './shared/message/message.service';
import {AccountService} from './shared/account/account.service';
import {Component, OnInit} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {CustomValidators} from "./shared/CustomValidators";
import "rxjs/add/operator/debounceTime";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {

  title = 'Primitive Bank';
  balance: number;
  checkBalanceSucceed: boolean;

  myForm: FormGroup;

  constructor(private accountService: AccountService, private messageService: MessageService,
              translate: TranslateService, private formBuilder: FormBuilder) {
    translate.setDefaultLang('en');
    translate.use('en');

    this.createForm();
  }

  ngOnInit() {
    this.checkBalance()
  }

  createForm() {
    this.myForm = this.formBuilder.group({
      accountId: ['1', Validators.required],
      amount: ['', [Validators.required, CustomValidators.number({min: 0})]]
    });

    this.myForm.get("accountId").valueChanges.debounceTime(50).subscribe(val => {
      if (this.myForm.value.accountId) {
        this.checkBalance()
      }
    })
  }

  deposit() {
    let accountId = this.myForm.value.accountId;
    let amount = this.myForm.value.amount;

    this.accountService.deposit(accountId, amount)
      .subscribe(resp => {
        this.postSuccessOperation()
      }, e => {
        console.log(e.error.key)
        this.messageService.setAsError('server.' + e.error.key)
      })
  }

  withdraw() {
    let accountId = this.myForm.value.accountId;
    let amount = this.myForm.value.amount;

    this.accountService.withdraw(accountId, amount)
      .subscribe(resp => {
        this.postSuccessOperation()
      }, e => {
        console.log(e.error.key)
        this.messageService.setAsError('server.' + e.error.key)
      })
  }

  checkBalance() {
    this.messageService.clear();

    this.accountService.checkBalance(this.myForm.value.accountId)
      .subscribe(resp => {
        console.log(resp)
        this.balance = resp.amount
        this.checkBalanceSucceed = true
      }, e => {
        console.log(e.error.key)
        this.balance = null
        this.checkBalanceSucceed = false
        this.messageService.setAsError('server.' + e.error.key)
      })
  }

  // --

  get accountId() {
    return this.myForm.get('accountId');
  }

  get amount() {
    return this.myForm.get('amount');
  }

  // ---

  private postSuccessOperation() {
    this.checkBalance()
    this.messageService.setAsSuccess('client.operation.success')
    this.myForm.get("amount").setValue("0.0")
  }

}
