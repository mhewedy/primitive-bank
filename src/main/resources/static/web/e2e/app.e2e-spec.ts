import {AppPage} from './app.po';

describe('web App', () => {
  let page: AppPage;

  beforeEach(() => {
    page = new AppPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to Primitive Bank!');
  });

  it('accountId text box to have account 1', () => {
    page.navigateTo();
    expect(page.getAccountIdText()).toEqual('1');
  });

  it('balance text box to have 100.5 as balance for account #1', () => {
    page.navigateTo();
    expect(page.getBalanceText()).toEqual('100.5');
  });
});
