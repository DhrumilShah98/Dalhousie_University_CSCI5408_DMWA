package backend.authentication.util;

import java.util.regex.Pattern;

public final class ValidationUtil {

  private ValidationUtil() {
    // Required private constructor. Cannot be instantiated.
  }

  public static boolean isUserNameValid(final String userName) {
    boolean isUserNameValid;
    if (userName == null || userName.isEmpty()) {
      isUserNameValid = false;
    } else {
      isUserNameValid = Pattern.matches(
          "[A-Za-z\\d]+",
          userName);
    }
    return isUserNameValid;
  }

  public static boolean isEmailValid(final String email) {
    boolean isEmailValid;
    if (email == null || email.isEmpty()) {
      isEmailValid = false;
    } else {
      isEmailValid = Pattern.matches(
          "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$",
          email);
    }
    return isEmailValid;
  }

  public static boolean isPasswordValid(final String password) {
    boolean isPasswordValid;
    if (password == null || password.isEmpty()) {
      isPasswordValid = false;
    } else {
      isPasswordValid = Pattern.matches(
          "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$",
          password);
    }
    return isPasswordValid;
  }

  public static boolean isSecurityAnswerValid(final String securityAnswer) {
    boolean isSecurityAnswerValid;
    if (securityAnswer == null || securityAnswer.isEmpty()) {
      isSecurityAnswerValid = false;
    } else {
      isSecurityAnswerValid = Pattern.matches(
          "[A-Za-z\\d]+",
          securityAnswer);
    }
    return isSecurityAnswerValid;
  }
}
