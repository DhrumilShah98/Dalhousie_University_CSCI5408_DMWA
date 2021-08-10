package backend.sql_dump_import.exception;

public final class SQLDumpImportException extends Exception {

  private final String errorMessage;

  public SQLDumpImportException(final String errorMessage) {
    super(errorMessage);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String toString() {
    return "SQLDumpImportException{" +
        "errorMessage='" + errorMessage + '\'' +
        '}';
  }
}