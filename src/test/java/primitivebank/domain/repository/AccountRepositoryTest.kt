package primitivebank.domain.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner
import primitivebank.domain.Account

@DataJpaTest
@RunWith(SpringRunner::class)
class AccountRepositoryTest {

    @Autowired
    lateinit var accountRepository: AccountRepository
    @Autowired
    lateinit var entityManager: TestEntityManager

    @Test
    fun `test check balance returns null for invalid accounts`() {

        val invalidAccountId = 100002341L

        val balance: Double? = accountRepository.checkBalance(invalidAccountId)

        assertThat(balance).isEqualTo(null)
    }

    @Test
    fun `test check balance never returns null for valid account`() {

        val fromDb = Account()
        entityManager.persist(fromDb)
        entityManager.flush()

        val balance = accountRepository.checkBalance(fromDb.id)

        assertThat(balance).isEqualTo(0.0)
    }


    @Test
    fun `test deposit on null balance`() {

        val fromDb = Account()
        entityManager.persist(fromDb)
        entityManager.flush()

        val success = accountRepository.deposit(fromDb.id, 100.0)
        val balance = accountRepository.checkBalance(fromDb.id)

        assertThat(success).isEqualTo(true)
        assertThat(balance).isEqualTo(100.0)
    }

    @Test
    fun `test deposit and with draw`() {

        val fromDb = Account()
        entityManager.persist(fromDb)
        entityManager.flush()

        accountRepository.deposit(fromDb.id, 100.0)
        accountRepository.deposit(fromDb.id, 100.0)
        accountRepository.withdraw(fromDb.id, 50.0)
        accountRepository.deposit(fromDb.id, 100.0)
        val balance = accountRepository.checkBalance(fromDb.id)

        assertThat(balance).isEqualTo(250.0)
    }
}
