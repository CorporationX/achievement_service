package faang.school.achievement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionalLockServiceTest {

    private static final int NAMESPACE = 1;
    private static final int LOCK_ID = 123;
    private static final String LOCK_QUERY = "select pg_advisory_xact_lock(?, ?)";
    @Mock
    private JdbcTemplate jdbcTemplate;
    @InjectMocks
    private TransactionalLockService transactionalLockService;
    @Captor
    private ArgumentCaptor<PreparedStatementCallback<Object>> callbackCaptor;

    @Test
    void runWithTransactionAndLock_SupplierExecutesLogicSuccessfully() throws Exception {
        String expectedResult = "Success";
        LockedOperationSupplier<String> supplier = () -> expectedResult;
        String actualResult = transactionalLockService.runWithTransactionAndLock(NAMESPACE, LOCK_ID, supplier);
        assertEquals(expectedResult, actualResult);
        verifyLockAcquisition(NAMESPACE, LOCK_ID);
    }

    @Test
    void runWithTransactionAndLock_RunnableExecutesLogicSuccessfully() throws Exception {
        LockedOperationRunnable runnable = mock(LockedOperationRunnable.class);
        transactionalLockService.runWithTransactionAndLock(NAMESPACE, LOCK_ID, runnable);
        verify(runnable, times(1)).run();
        verifyLockAcquisition(NAMESPACE, LOCK_ID);
    }

    @Test
    void runWithTransactionAndLock_SupplierThrowsRuntimeExceptionWhenLogicFails() {
        RuntimeException exception = new RuntimeException("Business logic error");
        LockedOperationSupplier<String> supplier = () -> {
            throw exception;
        };

        RuntimeException actualException = assertThrows(RuntimeException.class,
                                                        () -> transactionalLockService.runWithTransactionAndLock(
                                                            NAMESPACE, LOCK_ID, supplier));

        assertEquals("Business logic error", actualException.getMessage());
    }

    @Test
    void runWithTransactionAndLock_SupplierWrapsCheckedExceptionInIllegalStateException() {
        Exception checkedException = new Exception("Checked error");
        LockedOperationSupplier<String> supplier = () -> {
            throw checkedException;
        };

        IllegalStateException actualException = assertThrows(IllegalStateException.class,
                                                             () -> transactionalLockService.runWithTransactionAndLock(
                                                                 NAMESPACE, LOCK_ID, supplier));

        assertEquals("Locked work failed", actualException.getMessage());
        assertEquals(checkedException, actualException.getCause());
    }

    @Test
    void runWithTransactionAndLock_propagatesJdbcException() {
        DataAccessException jdbcError = new DataAccessException("DB connection failed") {};
        doThrow(jdbcError).when(jdbcTemplate).execute(eq(LOCK_QUERY), any(PreparedStatementCallback.class));

        LockedOperationSupplier<String> supplier = () -> "Should not be reached";

        assertThrows(DataAccessException.class,
                     () -> transactionalLockService.runWithTransactionAndLock(NAMESPACE, LOCK_ID, supplier));
    }

    private void verifyLockAcquisition(int expectedNs, int expectedId) throws SQLException {
        verify(jdbcTemplate).execute(eq(LOCK_QUERY), callbackCaptor.capture());

        PreparedStatementCallback<Object> callback = callbackCaptor.getValue();
        PreparedStatement psMock = mock(PreparedStatement.class);

        callback.doInPreparedStatement(psMock);

        verify(psMock).setInt(1, expectedNs);
        verify(psMock).setInt(2, expectedId);
        verify(psMock).execute();
    }
}