package backend.query_processor.exception;

public final class QueryProcessorException extends Exception {

  private final String errorMessage;

  public QueryProcessorException(final String errorMessage) {
    super(errorMessage);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String toString() {
    return "QueryProcessorException{" +
        "errorMessage='" + errorMessage + '\'' +
        '}';
  }
}