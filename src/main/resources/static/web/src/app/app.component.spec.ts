import {Amount} from './shared/amount.model';
import {TestBed, async, ComponentFixture, tick, fakeAsync} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {TranslateService} from '@ngx-translate/core';
import {Component, PipeTransform, Pipe, Injectable} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MessageService} from './shared/message/message.service';
import {AccountService} from './shared/account/account.service';
import {Observable} from "rxjs/Observable";
import 'rxjs/add/observable/of';
import 'rxjs/add/observable/throw';

describe('AppComponent', () => {

  let comp: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let accountService: AccountService;
  let messageService: MessageService;

  beforeEach(async(() => {

    TestBed.overrideComponent(AppComponent, {
      set: {
        providers: [
          {provide: AccountService, useClass: MockAccountService},
          {provide: MessageService, useClass: MockMessageService},
          {provide: TranslateService, useClass: TranslateServiceStub}
        ]
      }
    });

    TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [
        AppComponent,
        MockMessageComponent,
        TranslatePipeMock
      ]
    }).compileComponents();

  }));


  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    comp = fixture.componentInstance;
    comp.ngOnInit();
    accountService = fixture.debugElement.injector.get(AccountService);
    messageService = fixture.debugElement.injector.get(MessageService);
  });

  it('should create the app', async(() => {
    expect(comp).toBeTruthy();
  }));
  it(`should have as title 'app'`, async(() => {
    expect(comp.title).toEqual('Primitive Bank');
  }));
  it('should render title in a h1 tag', async(() => {
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('mocked.label.title Primitive Bank!');
  }));
  it('should have input text of type number for the account Id', async(() => {
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('#accountId').type).toBe('number');
  }));

  it('should have input text of type number for the amount', async(() => {
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('#amount').type).toBe('number');
  }));

  it('should have button for deposit', async(() => {
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('#deposit').type).toBe('button');
  }));

  it('should have button for withdraw', async(() => {
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('#withdraw').type).toBe('button');
  }));

  it('required validation should happen when the accountId field is empty', async(() => {
    comp.myForm.controls.accountId.setValue('');
    expect(comp.myForm.controls.accountId.errors.required).toBeTruthy()
  }));

  it('required validation should happen when the amount field is empty', async(() => {
    comp.myForm.controls.amount.setValue('');
    expect(comp.myForm.controls.amount.errors.required).toBeTruthy()
  }));

  it('number validation should happen when the amount field is zero', async(() => {
    comp.myForm.controls.amount.setValue(0);
    expect(comp.myForm.controls.amount.errors.number).toBeTruthy()
  }));

  it('number validation should not happen when the amount field is zero', async(() => {
    comp.myForm.controls.amount.setValue(300.20);
    expect(comp.myForm.controls.amount.errors).toBeNull()
  }));

  it('number validation should not happen when the amount field is zero with number less then 1', async(() => {
    comp.myForm.controls.amount.setValue(0.5);
    expect(comp.myForm.controls.amount.errors).toBeNull()
  }));

  it('set value to the accountId calls the method checkBalance', fakeAsync(() => {
    spyOn(comp, "checkBalance");
    comp.myForm.controls.accountId.setValue(3);
    tick(51);
    expect(comp.checkBalance).toHaveBeenCalledTimes(1);
  }));

  it('the call to checkBalance calls the service checkBalance', async(() => {
    spyOn(accountService, "checkBalance").and.returnValue(Observable.of(new Amount(100.0)));
    comp.checkBalance();
    expect(accountService.checkBalance).toHaveBeenCalledTimes(1);
  }));

  it('click the deposit button call the component deposit methods', async(() => {
    fixture.detectChanges();

    spyOn(comp, "deposit").and.returnValue(null);

    let button = fixture.debugElement.nativeElement.querySelector('#deposit');
    button.disabled = false;
    button.click();
    expect(comp.deposit).toHaveBeenCalledTimes(1);
  }));

  it('component deposit methods calls the service deposit method', async(() => {

    spyOn(accountService, "deposit").and.returnValue(Observable.of(""));
    comp.deposit();
    expect(accountService.deposit).toHaveBeenCalledTimes(1);
  }));

  it('click the withdraw button call the component withdraw methods', async(() => {
    fixture.detectChanges();

    spyOn(comp, "withdraw").and.returnValue(null);

    let button = fixture.debugElement.nativeElement.querySelector('#withdraw');
    button.disabled = false;
    button.click();
    expect(comp.withdraw).toHaveBeenCalledTimes(1);
  }));

  it('component withdraw methods calls the service withdraw method', async(() => {

    spyOn(accountService, "withdraw").and.returnValue(Observable.of(""));
    comp.withdraw();
    expect(accountService.withdraw).toHaveBeenCalledTimes(1);
  }));

  it('component checkBalance methods calls the service checkBalance method', async(() => {

    spyOn(accountService, "checkBalance").and.returnValue(Observable.of(new Amount(300.30)));
    comp.checkBalance();
    expect(accountService.checkBalance).toHaveBeenCalledTimes(1);
    expect(comp.balance).toEqual(300.30)
  }));

  it('component checkBalance methods calls the service checkBalance method => handle error case', async(() => {

    spyOn(accountService, "checkBalance").and.returnValue(Observable.throw(({"error": {"key": "error.message"}})));
    spyOn(messageService, "setAsError").and.callThrough();
    comp.checkBalance();

    expect(accountService.checkBalance).toHaveBeenCalledTimes(1);
    expect(comp.balance).toEqual(null);
    expect(messageService.setAsError).toHaveBeenCalledTimes(1);
    expect(messageService.setAsError).toHaveBeenCalledWith("server.error.message")
  }));

  it('component deposit methods calls the service deposit method => handle error case', async(() => {

    spyOn(accountService, "deposit").and.returnValue(Observable.throw(({"error": {"key": "error.message"}})));
    spyOn(messageService, "setAsError").and.callThrough();
    comp.deposit();

    expect(accountService.deposit).toHaveBeenCalledTimes(1);
    expect(messageService.setAsError).toHaveBeenCalledTimes(1);
    expect(messageService.setAsError).toHaveBeenCalledWith("server.error.message")
  }));

  it('component withdraw methods calls the service withdraw method => handle error case', async(() => {

    spyOn(accountService, "withdraw").and.returnValue(Observable.throw(({"error": {"key": "error.message"}})));
    spyOn(messageService, "setAsError").and.callThrough();
    comp.withdraw();

    expect(accountService.withdraw).toHaveBeenCalledTimes(1);
    expect(messageService.setAsError).toHaveBeenCalledTimes(1);
    expect(messageService.setAsError).toHaveBeenCalledWith("server.error.message")
  }));

});


@Component({
  selector: 'app-message',
  template: ''
})
class MockMessageComponent {
}

class MockAccountService {
  public checkBalance() {
    return Observable.of(new Amount(10));
  }

  public deposit() {
    return Observable.of("")
  }

  public withdraw() {
    return Observable.of("")
  }
}

class MockMessageService {
  public clear() {
  }

  public setAsSuccess(message: String) {
  }

  public setAsError(message: String) {
  }
}

class TranslateServiceStub {
  public get(key: any): any {
    Observable.of('mocked.' + key);
  }

  public setDefaultLang() {
  }

  public use() {
  }
}

@Pipe({
  name: "translate"
})
export class TranslatePipeMock implements PipeTransform {
  public name: string = "translate";

  public transform(query: string, ...args: any[]): any {
    return 'mocked.' + query;
  }
}
