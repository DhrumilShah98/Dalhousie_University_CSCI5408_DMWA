package backend.data_dictionary_generator.exception;

public final class DataDictionaryException extends Exception {

  private final String errorMessage;

  public DataDictionaryException(final String errorMessage) {
    super(errorMessage);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String toString() {
    return "DataDictionaryException{" +
        "errorMessage='" + errorMessage + '\'' +
        '}';
  }
}