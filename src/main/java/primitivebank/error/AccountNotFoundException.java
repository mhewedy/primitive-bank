package primitivebank.error;

import primitivebank.error.support.ErrorId;

public class AccountNotFoundException extends ClientException {

    public AccountNotFoundException() {
        super(ErrorId.AccountNotFoundException, "account.not.found");
    }
}
