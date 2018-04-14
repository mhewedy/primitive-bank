package primitivebank.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class OperationLog extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false, updatable = false)
    private Account account;

    @Enumerated(EnumType.STRING)
    private Type type;

    private Double amount;

    private Boolean success;

    public enum Type {
        DEPOSIT, WITHDRAW
    }

    public void assignToAccount(Long accountId) {
        Account account = new Account();
        account.setId(accountId);
        this.account = account;
    }

    public static OperationLog ofSucceededDeposit(Long accountId, Double amount) {
        OperationLog operationLog = of(accountId, amount);
        operationLog.setType(Type.DEPOSIT);
        operationLog.setSuccess(true);
        return operationLog;
    }

    public static OperationLog ofFailedDeposit(Long accountId, Double amount) {
        OperationLog operationLog = of(accountId, amount);
        operationLog.setType(Type.DEPOSIT);
        operationLog.setSuccess(false);
        return operationLog;
    }

    public static OperationLog ofSucceededWithdraw(Long accountId, Double amount) {
        OperationLog operationLog = of(accountId, amount);
        operationLog.setType(Type.WITHDRAW);
        operationLog.setSuccess(true);
        return operationLog;
    }

    public static OperationLog ofFailedWithdraw(Long accountId, Double amount) {
        OperationLog operationLog = of(accountId, amount);
        operationLog.setType(Type.WITHDRAW);
        operationLog.setSuccess(false);
        return operationLog;
    }

    // --- private

    private static OperationLog of(Long accountId, Double amount) {
        OperationLog operationLog = new OperationLog();
        operationLog.assignToAccount(accountId);
        operationLog.setAmount(amount);
        return operationLog;
    }
}
