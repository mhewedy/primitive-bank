package primitivebank.error;

public class AccountNotFoundException extends BankException {

    public AccountNotFoundException() {
        super(1, "account.not.found");
    }
}
