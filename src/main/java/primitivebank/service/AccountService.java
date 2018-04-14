package primitivebank.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import primitivebank.domain.OperationLog;
import primitivebank.domain.repository.AccountRepository;
import primitivebank.domain.repository.OperationLogRepository;
import primitivebank.error.AccountNotFoundException;
import primitivebank.error.DepositFailedException;
import primitivebank.error.NoEnoughBalanceException;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final OperationLogRepository operationLogRepository;

    public AccountService(AccountRepository accountRepository, OperationLogRepository operationLogRepository) {
        this.accountRepository = accountRepository;
        this.operationLogRepository = operationLogRepository;
    }

    @Transactional(noRollbackFor = DepositFailedException.class)
    public void deposit(Long accountId, double amount) {

        validateAccount(accountId);

        boolean success = accountRepository.deposit(accountId, amount);
        log.debug("deposit amount: {} in account: {} => success: {}", accountId, amount, success);

        if (success) {
            operationLogRepository.save(OperationLog.ofSucceededDeposit(accountId, amount));
        } else {
            operationLogRepository.save(OperationLog.ofFailedDeposit(accountId, amount));
            throw new DepositFailedException();
        }
    }

    @Transactional(noRollbackFor = NoEnoughBalanceException.class)
    public void withdraw(Long accountId, double amount) {

        validateAccount(accountId);

        boolean success = accountRepository.withdraw(accountId, amount);
        log.debug("withdraw amount: {} from account: {} => success: {}", accountId, amount, success);

        if (success) {
            operationLogRepository.save(OperationLog.ofSucceededWithdraw(accountId, amount));
        } else {
            operationLogRepository.save(OperationLog.ofFailedWithdraw(accountId, amount));
            throw new NoEnoughBalanceException();
        }
    }

    public Double checkBalance(Long accountId) {

        validateAccount(accountId);

        log.debug("checking balance for account: {}", accountId);
        return accountRepository.checkBalance(accountId);
    }

    // --- private

    private void validateAccount(Long accountId) {

        log.debug("validating account: {}", accountId);

        if (accountId == null ||
                !accountRepository.existsById(accountId)) {
            throw new AccountNotFoundException();
        }
    }
}
