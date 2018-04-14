package primitivebank.service;

import org.junit.Test;
import primitivebank.domain.OperationLog;
import primitivebank.domain.repository.AccountRepository;
import primitivebank.domain.repository.OperationLogRepository;
import primitivebank.error.AccountNotFoundException;
import primitivebank.error.DepositFailedException;
import primitivebank.error.NoEnoughBalanceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class AccountServiceTest {

    // -- deposit

    @Test(expected = AccountNotFoundException.class)
    public void testDepositWhenAccountNotExist() {

        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.existsById(anyLong())).thenReturn(false);

        AccountService accountService = new AccountService(accountRepository, null);
        accountService.deposit(null, 0.0);
    }

    @Test
    public void testDepositSuccessSavesIntoOperationLog() {

        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(accountRepository.deposit(anyLong(), anyDouble())).thenReturn(true);

        OperationLogRepository operationLogRepository = mock(OperationLogRepository.class);
        when(operationLogRepository.save(any())).thenReturn(new OperationLog());

        AccountService accountService = new AccountService(accountRepository, operationLogRepository);
        accountService.deposit(101L, 100.0);

        verify(operationLogRepository, times(1)).save(any());
    }

    @Test
    public void testDepositFailureSavesIntoOperationLog() {

        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(accountRepository.deposit(anyLong(), anyDouble())).thenReturn(false);

        OperationLogRepository operationLogRepository = mock(OperationLogRepository.class);
        when(operationLogRepository.save(any())).thenReturn(new OperationLog());

        AccountService accountService = new AccountService(accountRepository, operationLogRepository);

        try {
            accountService.deposit(101L, 100.0);
            fail("should throws exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(DepositFailedException.class);
        }

        verify(operationLogRepository, times(1)).save(any());
    }

    // -- withdraw

    @Test(expected = AccountNotFoundException.class)
    public void testWithdrawWhenAccountNotExist() {

        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.existsById(anyLong())).thenReturn(false);

        AccountService accountService = new AccountService(accountRepository, null);
        accountService.withdraw(null, 0.0);
    }

    @Test
    public void testWithdrawSuccessSavesIntoOperationLog() {

        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(accountRepository.withdraw(anyLong(), anyDouble())).thenReturn(true);

        OperationLogRepository operationLogRepository = mock(OperationLogRepository.class);
        when(operationLogRepository.save(any())).thenReturn(new OperationLog());

        AccountService accountService = new AccountService(accountRepository, operationLogRepository);
        accountService.withdraw(101L, 100.0);

        verify(operationLogRepository, times(1)).save(any());
    }

    @Test
    public void testWithdrawFailureSavesIntoOperationLog() {

        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(accountRepository.withdraw(anyLong(), anyDouble())).thenReturn(false);

        OperationLogRepository operationLogRepository = mock(OperationLogRepository.class);
        when(operationLogRepository.save(any())).thenReturn(new OperationLog());

        AccountService accountService = new AccountService(accountRepository, operationLogRepository);

        try {
            accountService.withdraw(101L, 100.0);
            fail("should throws exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NoEnoughBalanceException.class);
        }

        verify(operationLogRepository, times(1)).save(any());
    }

    // -- check balance

    @Test(expected = AccountNotFoundException.class)
    public void testCheckBalanceWhenAccountNotExist() {

        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.existsById(anyLong())).thenReturn(false);

        AccountService accountService = new AccountService(accountRepository, null);
        accountService.checkBalance(101L);
    }

    @Test
    public void testCheckBalanceReturnNull() {

        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(accountRepository.checkBalance(anyLong())).thenReturn(null);

        AccountService accountService = new AccountService(accountRepository, null);
        Double dbBalance = accountService.checkBalance(101L);

        assertThat(dbBalance).isNull();
    }

    @Test
    public void testCheckBalanceReturnSameRepositoryBalance() {

        AccountRepository accountRepository = mock(AccountRepository.class);
        when(accountRepository.existsById(anyLong())).thenReturn(true);
        when(accountRepository.checkBalance(anyLong())).thenReturn(4000.00);

        AccountService accountService = new AccountService(accountRepository, null);
        Double dbBalance = accountService.checkBalance(101L);

        assertThat(dbBalance).isEqualTo(4000.00);
    }
}
