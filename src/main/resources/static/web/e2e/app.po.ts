import {browser, by, element} from 'protractor';

export class AppPage {
  navigateTo() {
    return browser.get('/');
  }

  getParagraphText() {
    return element(by.css('app-root h1')).getText();
  }

  getAccountIdText() {
    return element(by.id('accountId')).getAttribute('value');
  }

  getBalanceText() {
    return element(by.id('balance')).getAttribute('value');
  }
}
