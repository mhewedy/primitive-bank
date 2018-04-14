package primitivebank.error.support;

public interface ErrorId {

    Integer AccountNotFoundException = 1;
    Integer NoEnoughBalanceException = 2;
    Integer DepositFailedException = 3;
    Integer MethodArgumentNotValidException = 4;
}
