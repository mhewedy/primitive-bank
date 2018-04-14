package primitivebank.error;

import primitivebank.error.support.ErrorId;

public class NoEnoughBalanceException extends ClientException {

    public NoEnoughBalanceException() {
        super(ErrorId.NoEnoughBalanceException, "no.enough.balance");
    }
}
