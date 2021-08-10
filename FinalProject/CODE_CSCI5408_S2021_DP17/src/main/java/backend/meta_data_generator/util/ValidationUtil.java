package backend.meta_data_generator.util;

import java.util.regex.Pattern;

public final class ValidationUtil {
  private ValidationUtil() {
    // Required private constructor. Cannot be instantiated.
  }

  public static boolean isDatabaseNameValid(final String databaseName) {
    boolean isDatabaseNameValid;
    if (databaseName == null || databaseName.isEmpty()) {
      isDatabaseNameValid = false;
    } else {
      isDatabaseNameValid = Pattern.matches(
          "[A-Za-z\\d]+",
          databaseName);
    }
    return isDatabaseNameValid;
  }

  public static boolean isTableNameValid(final String tableName) {
    boolean isTableNameValid;
    if (tableName == null || tableName.isEmpty()) {
      isTableNameValid = false;
    } else {
      isTableNameValid = Pattern.matches(
          "[A-Za-z\\d]+",
          tableName);
    }
    return isTableNameValid;
  }
}