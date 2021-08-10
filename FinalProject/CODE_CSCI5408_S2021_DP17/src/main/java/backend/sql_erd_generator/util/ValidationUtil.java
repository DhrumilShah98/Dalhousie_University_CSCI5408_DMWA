package backend.sql_erd_generator.util;

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
}