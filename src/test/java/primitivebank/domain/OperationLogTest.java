package primitivebank.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OperationLogTest {

    @Test
    public void ofSucceededDeposit() {
        OperationLog operationLog = OperationLog.ofSucceededDeposit(10L, 10.0);

        assertThat(operationLog.getAccount().getId()).isEqualTo(10L);
        assertThat(operationLog.getAmount()).isEqualTo(10.0);
        assertThat(operationLog.getSuccess()).isTrue();
        assertThat(operationLog.getMessage()).isNull();
        assertThat(operationLog.getType()).isEqualTo(OperationLog.Type.DEPOSIT);
    }

    @Test
    public void ofFailedDeposit() {

        OperationLog operationLog = OperationLog.ofFailedDeposit(10L, 10.0, "failed because of no reason");

        assertThat(operationLog.getAccount().getId()).isEqualTo(10L);
        assertThat(operationLog.getAmount()).isEqualTo(10.0);
        assertThat(operationLog.getSuccess()).isFalse();
        assertThat(operationLog.getMessage()).isEqualTo("failed because of no reason");
        assertThat(operationLog.getType()).isEqualTo(OperationLog.Type.DEPOSIT);
    }

    @Test
    public void ofSucceededWithdraw() {

        OperationLog operationLog = OperationLog.ofSucceededWithdraw(10L, 10.0);

        assertThat(operationLog.getAccount().getId()).isEqualTo(10L);
        assertThat(operationLog.getAmount()).isEqualTo(10.0);
        assertThat(operationLog.getSuccess()).isTrue();
        assertThat(operationLog.getMessage()).isNull();
        assertThat(operationLog.getType()).isEqualTo(OperationLog.Type.WITHDRAW);
    }

    @Test
    public void ofFailedWithdraw() {
        OperationLog operationLog = OperationLog.ofFailedWithdraw(10L, 10.0, "failed because no enough balance");

        assertThat(operationLog.getAccount().getId()).isEqualTo(10L);
        assertThat(operationLog.getAmount()).isEqualTo(10.0);
        assertThat(operationLog.getSuccess()).isFalse();
        assertThat(operationLog.getMessage()).isEqualTo("failed because no enough balance");
        assertThat(operationLog.getType()).isEqualTo(OperationLog.Type.WITHDRAW);
    }
}
