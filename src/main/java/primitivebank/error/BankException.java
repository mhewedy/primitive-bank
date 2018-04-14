package primitivebank.error;

public abstract class BankException extends RuntimeException {

    private int id;

    public BankException(int id, String key) {
        super(key);
        this.id = id;
    }
}
