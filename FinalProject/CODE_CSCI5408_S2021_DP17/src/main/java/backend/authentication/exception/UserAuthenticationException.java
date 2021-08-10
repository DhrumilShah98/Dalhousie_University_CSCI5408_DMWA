package backend.authentication.exception;

public final class UserAuthenticationException extends Exception {

  private final String errorMessage;

  public UserAuthenticationException(final String errorMessage) {
    super(errorMessage);
    this.errorMessage = errorMessage;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  @Override
  public String toString() {
    return "UserAuthenticationException{" +
        "errorMessage='" + errorMessage + '\'' +
        '}';
  }
}