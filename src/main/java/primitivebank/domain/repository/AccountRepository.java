package primitivebank.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import primitivebank.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    int UPDATE_SUCCESS_VAL = 1;

    @Query("select a.balance from Account a where a.id = :accountId")
    Double checkBalance(@Param("accountId") Long accountId);

    default boolean deposit(Long accountId, double amount) {
        return _deposit(accountId, amount) == UPDATE_SUCCESS_VAL;
    }

    default boolean withdraw(Long accountId, double amount) {
        return _withdraw(accountId, amount) == UPDATE_SUCCESS_VAL;
    }

    // -- internal

    @Modifying
    @Transactional
    @Query("update Account a " +
            "set a.balance = (a.balance + :amount) " +
            "where a.id = :accountId")
    int _deposit(@Param("accountId") Long accountId, @Param("amount") double amount);

    @Modifying
    @Transactional
    @Query("update Account a " +
            "set a.balance = (a.balance - :amount) " +
            "where a.id = :accountId and a.balance >= :amount")
    int _withdraw(@Param("accountId") Long accountId, @Param("amount") double amount);
}
