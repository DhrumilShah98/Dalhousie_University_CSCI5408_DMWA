package frontend.view;

import backend.authentication.controller.login.UserLoginController;
import backend.authentication.exception.UserAuthenticationException;
import backend.authentication.model.User;
import database.security_question.SecurityQuestion;
import frontend.print.CSCI5408DP17Printer;

import java.util.Scanner;

public final class UserLoginView {

  private final CSCI5408DP17Printer printer;
  private final Scanner scanner;

  public UserLoginView(final CSCI5408DP17Printer printer,
                       final Scanner scanner) {
    this.printer = printer;
    this.scanner = scanner;
  }

  public User performLogin() {
    printer.printContent("Enter username/email");
    final String user = scanner.nextLine();

    printer.printContent("Enter password");
    final String password = scanner.nextLine();

    final SecurityQuestion securityQuestion = SecurityQuestion.getInstance();
    final String randomSecurityQuestion = securityQuestion.getRandomSecurityQuestion();
    final int randomSecurityQuestionIndex = securityQuestion.getIndexByQuestion(randomSecurityQuestion);

    printer.printContent(randomSecurityQuestion);
    final String securityAnswer = scanner.nextLine();

    try {
      final UserLoginController userLoginController = new UserLoginController();
      final User loggedInUser =
          userLoginController.loginUser(user, password, securityAnswer, randomSecurityQuestionIndex);
      printer.printContent("Logged in as: " + loggedInUser.getUserName());
      return loggedInUser;
    } catch (final UserAuthenticationException e) {
      printer.printContent(e.toString());
      return null;
    }
  }
}