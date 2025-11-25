package faang.school.achievement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionalLockService {
    private static final String LOCK_REQUEST = "select pg_advisory_xact_lock(?, ?)";
    private final JdbcTemplate jdbc;

    @Transactional
    public <T> T runWithTransactionAndLock(int ns, int id, LockedOperationSupplier<T> work) {
        jdbc.execute(LOCK_REQUEST, (PreparedStatementCallback<Void>) ps -> {
            ps.setInt(1, ns);
            ps.setInt(2, id);
            ps.execute();
            return null;
        });

        try {
            return work.get();
        } catch (RuntimeException e) {
            log.error("Runtime error in locked work (ns={}, id={}): {}", ns, id, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalStateException("Locked work failed", e);
        }
    }

    @Transactional
    public void runWithTransactionAndLock(int ns, int id, LockedOperationRunnable work) {
        runWithTransactionAndLock(ns, id, () -> {
            work.run();
            return null;
        });
    }
}