package backend.authentication.controller.login;

import backend.authentication.constant.Constant;
import backend.authentication.exception.UserAuthenticationException;
import backend.authentication.model.User;
import backend.authentication.session.BackendSession;
import backend.authentication.util.HashAlgorithmUtil;
import backend.authentication.util.ValidationUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public final class UserLoginController {

  private void validateUserCredentials(final String user,
                                       final String password,
                                       final String securityAnswer)
      throws UserAuthenticationException {
    final boolean isUserNameValid = ValidationUtil.isUserNameValid(user);
    final boolean isEmailValid = ValidationUtil.isEmailValid(user);
    if (!isUserNameValid && !isEmailValid) {
      throw new UserAuthenticationException("Invalid credentials!");
    }
    final boolean isPasswordValid = ValidationUtil.isPasswordValid(password);
    if (!isPasswordValid) {
      throw new UserAuthenticationException("Invalid credentials!");
    }
    final boolean isSecurityAnswerValid = ValidationUtil.isSecurityAnswerValid(securityAnswer);
    if (!isSecurityAnswerValid) {
      throw new UserAuthenticationException("Invalid credentials!");
    }
  }

  private boolean validateUserExists(final String user,
                                     final String password,
                                     final String securityAnswer,
                                     final int securityAnswerIndex,
                                     final String[] userDetailsArr)
      throws NoSuchAlgorithmException {
    boolean userExists;
    final boolean isSameUserName = userDetailsArr[1].equals(user);
    final boolean isSameEmail = userDetailsArr[2].equals(user);
    if (isSameUserName || isSameEmail) {
      final boolean isSamePassword = HashAlgorithmUtil.validateSHA256Hash(password, userDetailsArr[3]);
      if (isSamePassword) {
        userExists = userDetailsArr[securityAnswerIndex].equals(securityAnswer);
      } else {
        userExists = false;
      }
    } else {
      userExists = false;
    }
    return userExists;
  }

  private User prepareUser(final String[] userDetailsArr) {
    final long id = Long.parseLong(userDetailsArr[0]);
    final String userName = userDetailsArr[1];
    final String email = userDetailsArr[2];
    final String password = userDetailsArr[3];
    final String securityQ1Ans = userDetailsArr[4];
    final String securityQ2Ans = userDetailsArr[5];
    final String securityQ3Ans = userDetailsArr[6];
    final String securityQ4Ans = userDetailsArr[7];
    return new User(id,
        userName,
        email,
        password,
        securityQ1Ans,
        securityQ2Ans,
        securityQ3Ans,
        securityQ4Ans);
  }

  private User login(final String user,
                     final String password,
                     final String securityAnswer,
                     final int securityAnswerIndex)
      throws UserAuthenticationException {
    try (final BufferedReader usersFileReader = new BufferedReader(new FileReader(Constant.USERS_FILE))) {
      String userDetails;
      while ((userDetails = usersFileReader.readLine()) != null) {
        final String[] userDetailsArr = userDetails.split(" ");
        final boolean userExists = validateUserExists(user, password, securityAnswer, securityAnswerIndex, userDetailsArr);
        if (userExists) {
          return prepareUser(userDetailsArr);
        }
      }
      throw new UserAuthenticationException("Invalid credentials!");
    } catch (final IOException | NoSuchAlgorithmException e) {
      throw new UserAuthenticationException("Something went wrong. Please try again after sometime!");
    }
  }

  public User loginUser(final String user,
                        final String password,
                        final String securityAnswer,
                        final int securityAnswerIndex)
      throws UserAuthenticationException {
    validateUserCredentials(user, password, securityAnswer);
    final User loggedInUser = login(user, password, securityAnswer, securityAnswerIndex);
    BackendSession.setLoggedInUser(loggedInUser);
    return loggedInUser;
  }
}