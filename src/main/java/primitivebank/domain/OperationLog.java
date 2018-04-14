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

    private String message;

    public enum Type {
        DEPOSIT, WITHDRAW
    }

    public void assignToAccount(Long accountId) {
        Account account = new Account();
        account.setId(accountId);
        this.account = account;
    }

    public static OperationLog ofSucceededDeposit(Long accountId, Double amount) {
        return fill(of(accountId, amount), Type.DEPOSIT, true, null);
    }

    public static OperationLog ofFailedDeposit(Long accountId, Double amount, String message) {
        return fill(of(accountId, amount), Type.DEPOSIT, false, message);
    }

    public static OperationLog ofSucceededWithdraw(Long accountId, Double amount) {
        return fill(of(accountId, amount), Type.WITHDRAW, true, null);
    }

    public static OperationLog ofFailedWithdraw(Long accountId, Double amount, String message) {
        return fill(of(accountId, amount), Type.WITHDRAW, false, message);
    }

    // --- private

    private static OperationLog of(Long accountId, Double amount) {
        OperationLog operationLog = new OperationLog();
        operationLog.assignToAccount(accountId);
        operationLog.setAmount(amount);
        return operationLog;
    }

    private static OperationLog fill(OperationLog operationLog, Type type, boolean success, String message) {
        operationLog.setType(type);
        operationLog.setSuccess(success);
        operationLog.setMessage(message);
        return operationLog;
    }
}
