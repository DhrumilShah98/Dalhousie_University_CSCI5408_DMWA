package backend.sql_dump_generator.exception;

public final class SQLDumpException extends Exception {

  private final String errorMessage;

  public SQLDumpException(final String errorMessage) {
    super(errorMessage);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String toString() {
    return "SQLDumpException{" +
        "errorMessage='" + errorMessage + '\'' +
        '}';
  }
}