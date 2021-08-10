package backend.sql_erd_generator.exception;

public final class SQLERDException extends Exception {

  private final String errorMessage;

  public SQLERDException(final String errorMessage) {
    super(errorMessage);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String toString() {
    return "SQLERDException{" +
        "errorMessage='" + errorMessage + '\'' +
        '}';
  }
}