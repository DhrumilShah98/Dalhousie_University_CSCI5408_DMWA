package backend.query_processor.lock;

import backend.query_processor.exception.QueryProcessorException;

import java.util.HashMap;
import java.util.Map;

public final class Lock {

  private Lock() {
    // Required private constructor. Cannot be instantiated
  }

  public enum LockType {
    SHARED,
    EXCLUSIVE
  }

  public static int sharedLockCounter = 0;

  public static final Map<String, LockType> allLocks =
      new HashMap<>();

  public static void applySharedLock(final String databaseName,
                                     final String tableName)
      throws QueryProcessorException {
    final String databasePath = databaseName + "." + "null";
    final Lock.LockType databaseLockTypeApplied = Lock.allLocks.get(databasePath);
    if (databaseLockTypeApplied != null) {
      throw new QueryProcessorException("Database " + databaseName + " locked!");
    }
    final String tablePath = databaseName + "." + tableName;
    final Lock.LockType tableLockTypeApplied = Lock.allLocks.get(tablePath);
    if (tableLockTypeApplied == null) {
      Lock.allLocks.put(tablePath, Lock.LockType.SHARED);
      Lock.sharedLockCounter++;
    }
    if (tableLockTypeApplied == Lock.LockType.EXCLUSIVE) {
      throw new QueryProcessorException("Read or write operations cannot be performed on table " + tableName + " at this moment!");
    }
    if (tableLockTypeApplied == Lock.LockType.SHARED) {
      Lock.sharedLockCounter++;
    }
  }

  public static void releaseSharedLock(final String databaseName,
                                       final String tableName) {
    final String tablePath = databaseName + "." + tableName;
    Lock.sharedLockCounter--;
    if (Lock.sharedLockCounter == 0) {
      Lock.allLocks.remove(tablePath);
    }
  }

  public static void applyExclusiveLock(final String databaseName,
                                        final String tableName)
      throws QueryProcessorException {
    if (tableName == null) {
      final String databasePath = databaseName + "." + "null";
      final Lock.LockType databaseLockTypeApplied = Lock.allLocks.get(databasePath);
      if (databaseLockTypeApplied == null) {
        Lock.allLocks.put(databasePath, Lock.LockType.EXCLUSIVE);
      } else {
        throw new QueryProcessorException("Database " + databaseName + " locked!");
      }
    } else {
      final String tablePath = databaseName + "." + tableName;
      final Lock.LockType tableLockTypeApplied = Lock.allLocks.get(tablePath);
      if (tableLockTypeApplied == null) {
        Lock.allLocks.put(tablePath, Lock.LockType.EXCLUSIVE);
      } else {
        throw new QueryProcessorException("Table " + tableName + " locked!");
      }
    }
  }

  public static void releaseExclusiveLock(final String databaseName,
                                          final String tableName) {
    if (tableName == null) {
      final String databasePath = databaseName + "." + "null";
      Lock.allLocks.remove(databasePath);
    } else {
      final String tablePath = databaseName + "." + tableName;
      Lock.allLocks.remove(tablePath);
    }
  }
}