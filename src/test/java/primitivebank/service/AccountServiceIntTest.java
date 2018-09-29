package primitivebank.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import primitivebank.domain.Account;
import primitivebank.domain.OperationLog;
import primitivebank.domain.repository.AccountRepository;
import primitivebank.error.AccountNotFoundException;
import primitivebank.error.DepositFailedException;
import primitivebank.error.NoEnoughBalanceException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import static primitivebank.QueryUtil.getList;
import static primitivebank.QueryUtil.getObject;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceIntTest {

    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    @Before
    public void before() {
        // some test cases change the reference to the accountRepository in favor of mocked instance
        setField(accountService, "accountRepository", accountRepository);
    }

    // -- deposit

    @Test(expected = AccountNotFoundException.class)
    public void testDepositForInvalidAccount() {

        accountService.deposit(null, 100.0);
    }

    @Test
    @Transactional
    public void testDepositForNewlyCreatedAccount() {

        Account fromDb = accountRepository.save(new Account());
        accountService.deposit(fromDb.getId(), 100.0);

        Double dbBalance = getObject(entityManager, "select balance from Account where id = ?", fromDb.getId());
        OperationLog operationLog =
                getObject(entityManager, "select o from OperationLog o where o.account.id = ?", fromDb.getId());

        OperationLog expected = OperationLog.ofSucceededDeposit(fromDb.getId(), 100.0);
        assertThat(dbBalance).isEqualTo(100.0);
        assertThat(operationLog).isEqualToComparingOnlyGivenFields(expected, "type", "amount", "success");
    }

    @Test
    @Transactional
    public void testDeposit_WhenItFailsBecauseOfDepositFailedException_ShouldSaveOperationLogAsWell() {
        // setup
        Account fromDb = this.accountRepository.save(new Account());

        AccountRepository mockedAccountRepo = Mockito.mock(AccountRepository.class);
        setField(accountService, "accountRepository", mockedAccountRepo);

        when(mockedAccountRepo.existsById(fromDb.getId())).thenReturn(true);
        when(mockedAccountRepo.deposit(fromDb.getId(), 100.0)).thenReturn(false);

        // when
        try {
            accountService.deposit(fromDb.getId(), 100.0);
            fail("should throws exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(DepositFailedException.class);
        }

        // then
        Double dbBalance = getObject(entityManager, "select balance from Account where id = ?", fromDb.getId());
        OperationLog operationLog =
                getObject(entityManager, "select o from OperationLog o where o.account.id = ?", fromDb.getId());

        OperationLog expected = OperationLog.ofFailedDeposit(fromDb.getId(), 100.0, "deposit.failed");
        assertThat(dbBalance).isEqualTo(0.0);
        assertThat(operationLog).isEqualToComparingOnlyGivenFields(expected, "type", "amount", "success", "message");
    }

    @Test
    @Transactional
    public void testDeposit_WhenItFailsBecauseOfAnyOtherRuntimeException_ShouldRollbackEveryThing() {
        // setup
        Account fromDb = accountRepository.save(new Account());

        AccountRepository mockedAccountRepo = Mockito.mock(AccountRepository.class);
        setField(accountService, "accountRepository", mockedAccountRepo);

        when(mockedAccountRepo.existsById(fromDb.getId())).thenReturn(true);
        when(mockedAccountRepo.deposit(fromDb.getId(), 100.0)).thenThrow(new RuntimeException("bla bla"));

        // when
        try {
            accountService.deposit(fromDb.getId(), 100.0);
            fail("should throws exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(RuntimeException.class);
        }

        // then
        Double dbBalance = getObject(entityManager, "select balance from Account where id = ?", fromDb.getId());
        OperationLog operationLog =
                getObject(entityManager, "select o from OperationLog o where o.account.id = ?", fromDb.getId());

        assertThat(dbBalance).isEqualTo(0.0);
        assertThat(operationLog).isNull();
    }


    // -- withdraw

    @Test(expected = AccountNotFoundException.class)
    public void testWithdrawForInvalidAccount() {

        accountService.withdraw(null, 100.0);
    }

    @Test
    @Transactional
    public void testWithdrawForNewlyCreatedAccount() {

        Account fromDb = accountRepository.save(new Account());

        try {
            accountService.withdraw(fromDb.getId(), 100.0);
            fail("should throws exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(NoEnoughBalanceException.class);
        }

        Double dbBalance = getObject(entityManager, "select balance from Account where id = ?", fromDb.getId());
        OperationLog operationLog =
                getObject(entityManager, "select o from OperationLog o where o.account.id = ?", fromDb.getId());

        OperationLog expected = OperationLog.ofFailedWithdraw(fromDb.getId(), 100.0, "no.enough.balance");
        assertThat(dbBalance).isEqualTo(0.0);
        assertThat(operationLog).isEqualToComparingOnlyGivenFields(expected, "type", "amount", "success", "message");
    }

    @Test
    @Transactional
    public void testWithdrawAfterDeposit() {

        Account fromDb = accountRepository.save(new Account());

        accountService.deposit(fromDb.getId(), 700.90);
        accountService.withdraw(fromDb.getId(), 50.80);

        Double dbBalance = getObject(entityManager, "select balance from Account where id = ?", fromDb.getId());
        List<OperationLog> operationLog =
                getList(entityManager, "select o from OperationLog o where o.account.id = ?", fromDb.getId());

        OperationLog expected1 = OperationLog.ofSucceededDeposit(fromDb.getId(), 700.90);
        OperationLog expected2 = OperationLog.ofSucceededWithdraw(fromDb.getId(), 50.80);

        assertThat(dbBalance).isEqualTo(650.10);

        assertThat(operationLog.get(0)).isEqualToComparingOnlyGivenFields(expected1, "type", "amount", "success");
        assertThat(operationLog.get(1)).isEqualToComparingOnlyGivenFields(expected2, "type", "amount", "success");
    }

    @Test
    @Transactional
    public void testWithdraw_WhenItFailsBecauseOfAnyOtherRuntimeException_ShouldRollbackEveryThing() {
        // setup
        Account fromDb = accountRepository.save(new Account());
        fromDb.setBalance(1000.00);
        fromDb = accountRepository.save(fromDb);

        AccountRepository mockedAccountRepo = Mockito.mock(AccountRepository.class);
        setField(accountService, "accountRepository", mockedAccountRepo);

        when(mockedAccountRepo.existsById(fromDb.getId())).thenReturn(true);
        when(mockedAccountRepo.withdraw(fromDb.getId(), 100.0)).thenThrow(new RuntimeException("bla bla"));

        // when
        try {
            accountService.withdraw(fromDb.getId(), 100.0);
            fail("should throws exception");
        } catch (Exception ex) {
            assertThat(ex).isInstanceOf(RuntimeException.class);
        }

        // then
        Double dbBalance = getObject(entityManager, "select balance from Account where id = ?", fromDb.getId());
        OperationLog operationLog =
                getObject(entityManager, "select o from OperationLog o where o.account.id = ?", fromDb.getId());

        assertThat(dbBalance).isEqualTo(1000.0);
        assertThat(operationLog).isNull();
    }


    @Test
    //@Transactional    // Transactional associate the current thread to the Tnx,
    // and then no other threads can see the data until the tnx completes for rollbacks
    public void testConcurrentDeposits() throws Exception {

        Account fromDb = accountRepository.save(new Account());

        ExecutorService pool = Executors.newCachedThreadPool();

        CountDownLatch latch = new CountDownLatch(5);

        pool.submit(() -> deposit(fromDb.getId(), 100.0, latch));
        pool.submit(() -> deposit(fromDb.getId(), 100.0, latch));
        pool.submit(() -> deposit(fromDb.getId(), 100.0, latch));
        pool.submit(() -> deposit(fromDb.getId(), 100.0, latch));
        pool.submit(() -> deposit(fromDb.getId(), 100.0, latch));

        latch.await(1, TimeUnit.SECONDS);

        Double dbBalance = accountService.checkBalance(fromDb.getId());
        assertThat(dbBalance).isEqualTo(500.0);

        pool.shutdown();
    }

    @Test
    //@Transactional    // Transactional associate the current thread to the Tnx,
    // and then no other threads can see the data until the tnx completes for rollbacks
    public void testConcurrentDepositsInTwoDifferentAccounts() throws Exception {

        Account fromDb1 = accountRepository.save(new Account());
        Account fromDb2 = accountRepository.save(new Account());

        ExecutorService pool = Executors.newCachedThreadPool();

        CountDownLatch latch = new CountDownLatch(5);

        pool.submit(() -> deposit(fromDb1.getId(), 100.0, latch));
        pool.submit(() -> deposit(fromDb1.getId(), 100.0, latch));
        pool.submit(() -> deposit(fromDb1.getId(), 100.0, latch));
        pool.submit(() -> deposit(fromDb2.getId(), 100.0, latch));
        pool.submit(() -> deposit(fromDb2.getId(), 100.0, latch));

        latch.await(1, TimeUnit.SECONDS);

        Double dbBalance1 = accountService.checkBalance(fromDb1.getId());
        assertThat(dbBalance1).isEqualTo(300.0);
        Double dbBalance2 = accountService.checkBalance(fromDb2.getId());
        assertThat(dbBalance2).isEqualTo(200.0);

        pool.shutdown();
    }

    @Test
    //@Transactional    // Transactional associates the current thread to the Tnx,
    // and then no other threads can see the data until the tnx completes for rollbacks
    public void testConcurrentWithdraws() throws Exception {

        Account fromDb = accountRepository.save(new Account());

        accountService.deposit(fromDb.getId(), 100.0);

        ExecutorService pool = Executors.newCachedThreadPool();

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        CountDownLatch latch = new CountDownLatch(3);

        pool.submit(() -> withdraw(fromDb.getId(), 100.0, latch, successCount, failureCount));
        pool.submit(() -> withdraw(fromDb.getId(), 100.0, latch, successCount, failureCount));
        pool.submit(() -> withdraw(fromDb.getId(), 100.0, latch, successCount, failureCount));

        latch.await(1, TimeUnit.SECONDS);

        Double dbBalance = accountService.checkBalance(fromDb.getId());
        assertThat(dbBalance).isEqualTo(0.0);

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failureCount.get()).isEqualTo(2);

        pool.shutdown();
    }

    @Test
    //@Transactional    // Transactional associates the current thread to the Tnx,
    // and then no other threads can see the data until the tnx completes for rollbacks
    public void testConcurrentWithdrawsFromTwoDifferentAccounts() throws Exception {

        Account fromDb1 = accountRepository.save(new Account());
        accountService.deposit(fromDb1.getId(), 100.0);

        Account fromDb2 = accountRepository.save(new Account());
        accountService.deposit(fromDb2.getId(), 1000.0);

        ExecutorService pool = Executors.newCachedThreadPool();

        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        CountDownLatch latch = new CountDownLatch(3);

        pool.submit(() -> withdraw(fromDb1.getId(), 100.0, latch, successCount, failureCount));
        pool.submit(() -> withdraw(fromDb1.getId(), 100.0, latch, successCount, failureCount));
        pool.submit(() -> withdraw(fromDb2.getId(), 1100.0, latch, successCount, failureCount));

        latch.await(1, TimeUnit.SECONDS);

        Double dbBalance1 = accountService.checkBalance(fromDb1.getId());
        assertThat(dbBalance1).isEqualTo(0.0);

        Double dbBalance2 = accountService.checkBalance(fromDb2.getId());
        assertThat(dbBalance2).isEqualTo(1000.0);

        assertThat(successCount.get()).isEqualTo(1);
        assertThat(failureCount.get()).isEqualTo(2);

        pool.shutdown();
    }

    private void deposit(Long accountId, double amount, CountDownLatch latch) {
        //System.out.printf("deposit concurrently for amount: %s\n", amount);
        accountService.deposit(accountId, amount);
        latch.countDown();
    }

    private void withdraw(Long accountId, double amount, CountDownLatch latch,
                          AtomicInteger successCount, AtomicInteger failureCount) {
        //System.out.printf("withdraw concurrently for amount: %s\n", amount);
        try {
            accountService.withdraw(accountId, amount);
            successCount.incrementAndGet();
        } catch (NoEnoughBalanceException ex) {
            failureCount.incrementAndGet();
        }
        latch.countDown();
    }
}
