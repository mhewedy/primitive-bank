package primitivebank.error;

public class NoEnoughBalanceException extends BankException {

    public NoEnoughBalanceException() {
        super(2, "no.enough.balance");
    }
}
