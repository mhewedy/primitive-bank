package primitivebank.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import primitivebank.domain.OperationLog;

public interface OperationLogRepository extends JpaRepository<OperationLog, Long> {
}
