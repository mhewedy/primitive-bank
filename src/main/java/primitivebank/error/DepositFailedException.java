package primitivebank.error;

import primitivebank.error.support.ErrorId;

public class DepositFailedException extends ServerException {

    public DepositFailedException() {
        super(ErrorId.DepositFailedException, "deposit.failed");
    }
}
