package primitivebank.error;

public class DepositFailedException extends BankException {

    public DepositFailedException() {
        super(3, "deposit.failed");
    }
}
