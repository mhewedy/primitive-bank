package primitivebank.domain.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import primitivebank.domain.Account;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testCheckBalanceReturnNullForInvalidAccounts() {

        long invalidAccountId = 100002341L;

        Double balance = accountRepository.checkBalance(invalidAccountId);

        assertThat(balance).isEqualTo(null);
    }

    @Test
    public void testCheckBalanceNeverReturnsNullForValidAccountIds() {

        Account fromDb = new Account();
        entityManager.persist(fromDb);
        entityManager.flush();

        Double balance = accountRepository.checkBalance(fromDb.getId());

        assertThat(balance).isEqualTo(0);
    }


    @Test
    public void testDepositOnNullBalance() {

        Account fromDb = new Account();
        entityManager.persist(fromDb);
        entityManager.flush();

        boolean success = accountRepository.deposit(fromDb.getId(), 100.0);
        Double balance = accountRepository.checkBalance(fromDb.getId());

        assertThat(success).isEqualTo(true);
        assertThat(balance).isEqualTo(100.0);
    }

    @Test
    public void testDepositAndWithDraw() {

        Account fromDb = new Account();
        entityManager.persist(fromDb);
        entityManager.flush();

        accountRepository.deposit(fromDb.getId(), 100.0);
        accountRepository.deposit(fromDb.getId(), 100.0);
        accountRepository.withdraw(fromDb.getId(), 50.0);
        accountRepository.deposit(fromDb.getId(), 100.0);
        Double balance = accountRepository.checkBalance(fromDb.getId());

        assertThat(balance).isEqualTo(250.0);
    }
}
