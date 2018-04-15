import { Amount } from './shared/amount.model';
import { TestBed, async } from '@angular/core/testing';
import { AppComponent } from './app.component';
import {TranslateModule, TranslateLoader, TranslateService} from '@ngx-translate/core';
import {HttpClientModule, HttpClient} from '@angular/common/http';
import { Component, PipeTransform, Pipe} from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MessageService } from './shared/message/message.service';
import { AccountService } from './shared/account/account.service';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import { Observable } from "rxjs/Observable";
import 'rxjs/add/observable/of';

describe('AppComponent', () => {
  beforeEach(async(() => {

    TestBed.overrideComponent(AppComponent, {
      set: {
        providers: [
          { provide: AccountService, useClass: MockAccountService },
          { provide: MessageService, useClass: MockMessageService },
          {provide: TranslateService, useClass: TranslateServiceStub}
        ]
      }
    }); 

    TestBed.configureTestingModule({
      imports: [FormsModule],
      declarations: [
        AppComponent,
        MockMessageComponent,
        TranslatePipeMock
      ]
    }).compileComponents();
  
  }));
  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app).toBeTruthy();
  }));
  it(`should have as title 'app'`, async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.debugElement.componentInstance;
    expect(app.title).toEqual('Primitive Bank');
  }));
  it('should render title in a h1 tag', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('h1').textContent).toContain('mocked.label.title Primitive Bank!');
  }));
  it('should have input text of type number for the account Id', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('#accountId').type).toBe('number');
  }));

  it('should have input text of type number for the amount', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('#amount').type).toBe('number');
  }));

  it('should have button for deposit', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('#deposit').type).toBe('button');
  }));

  it('should have button for withdraw', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('#withdraw').type).toBe('button');
  }));

});


@Component({
  selector: 'app-message',
  template: ''
})
class MockMessageComponent {
}

class MockAccountService {
  public checkBalance(){
    return Observable.of(new Amount(10));
  }
}

class MockMessageService {
  public clear(){}
}

class TranslateServiceStub{  
    public get(key: any): any {
      Observable.of('mocked.' + key);
    }
    public setDefaultLang(){}
    public use(){}
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
  