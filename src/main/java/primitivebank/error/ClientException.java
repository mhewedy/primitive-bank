package primitivebank.error;

public abstract class ClientException extends BankException {

    public ClientException(int id, String key) {
        super(id, key);
    }
}
