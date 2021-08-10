package backend.query_parser.exception;

public final class QueryParserException extends Exception {

  private final String errorMessage;

  public QueryParserException(final String errorMessage) {
    super(errorMessage);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String toString() {
    return "QueryParserException{" +
        "errorMessage='" + errorMessage + '\'' +
        '}';
  }
}