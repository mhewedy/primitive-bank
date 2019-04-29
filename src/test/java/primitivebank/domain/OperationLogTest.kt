package primitivebank.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class OperationLogTest {

    @Test
    fun `test ofSucceededDeposit`() {
        val operationLog = OperationLog.ofSucceededDeposit(10L, 10.0)

        assertThat(operationLog.account.id).isEqualTo(10L)
        assertThat(operationLog.amount).isEqualTo(10.0)
        assertThat(operationLog.success).isTrue()
        assertThat(operationLog.message).isNull()
        assertThat(operationLog.type).isEqualTo(OperationLog.Type.DEPOSIT)
    }

    @Test
    fun `test  ofFailedDeposit`() {

        val operationLog = OperationLog.ofFailedDeposit(10L, 10.0, "failed because of no reason")

        assertThat(operationLog.account.id).isEqualTo(10L)
        assertThat(operationLog.amount).isEqualTo(10.0)
        assertThat(operationLog.success).isFalse()
        assertThat(operationLog.message).isEqualTo("failed because of no reason")
        assertThat(operationLog.type).isEqualTo(OperationLog.Type.DEPOSIT)
    }

    @Test
    fun `test ofSucceededWithdraw`() {

        val operationLog = OperationLog.ofSucceededWithdraw(10L, 10.0)

        assertThat(operationLog.account.id).isEqualTo(10L)
        assertThat(operationLog.amount).isEqualTo(10.0)
        assertThat(operationLog.success).isTrue()
        assertThat(operationLog.message).isNull()
        assertThat(operationLog.type).isEqualTo(OperationLog.Type.WITHDRAW)
    }

    @Test
    fun `test ofFailedWithdraw`() {
        val operationLog = OperationLog.ofFailedWithdraw(10L, 10.0, "failed because no enough balance")

        assertThat(operationLog.account.id).isEqualTo(10L)
        assertThat(operationLog.amount).isEqualTo(10.0)
        assertThat(operationLog.success).isFalse()
        assertThat(operationLog.message).isEqualTo("failed because no enough balance")
        assertThat(operationLog.type).isEqualTo(OperationLog.Type.WITHDRAW)
    }
}
