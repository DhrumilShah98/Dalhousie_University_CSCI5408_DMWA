package backend.meta_data_generator.exception;

public final class MetaDataException extends Exception {

  private final String errorMessage;

  public MetaDataException(final String errorMessage) {
    super(errorMessage);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String toString() {
    return "MetaDataException{" +
        "errorMessage='" + errorMessage + '\'' +
        '}';
  }
}