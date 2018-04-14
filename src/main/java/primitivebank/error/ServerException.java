package primitivebank.error;

public abstract class ServerException extends BankException {

    public ServerException(int id, String key) {
        super(id, key);
    }
}
