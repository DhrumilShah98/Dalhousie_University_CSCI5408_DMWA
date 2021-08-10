package frontend.view;

import backend.authentication.controller.registration.UserRegistrationController;
import backend.authentication.exception.UserAuthenticationException;
import backend.authentication.model.User;
import database.security_question.SecurityQuestion;
import frontend.print.CSCI5408DP17Printer;

import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public final class UserRegistrationView {
  private final CSCI5408DP17Printer printer;
  private final Scanner scanner;

  public UserRegistrationView(final CSCI5408DP17Printer printer,
                              final Scanner scanner) {
    this.printer = printer;
    this.scanner = scanner;
  }

  public void performUserRegistration() {
    printer.printContent("Enter username(Must be alphanumeric characters only)");
    final String userName = scanner.nextLine();

    printer.printContent("Enter email(Example: johndoe@gmail.com)");
    final String email = scanner.nextLine();

    printer.printContent("Enter password (1 small letter, 1 capital letter, 1 special char, and 1 number and " +
        "min length 8 and max length 20)");
    final String password = scanner.nextLine();

    final SecurityQuestion securityQuestion = SecurityQuestion.getInstance();
    final Map<Integer, String> securityQuestionsMap = securityQuestion.getSecurityQuestionsMap();
    final ArrayList<String> securityQuestionAnswers = new ArrayList<>();
    for (Map.Entry<Integer, String> entry : securityQuestionsMap.entrySet()) {
      printer.printContent(entry.getValue());
      securityQuestionAnswers.add(scanner.nextLine());
    }

    final User user = new User(
        userName,
        email,
        password,
        securityQuestionAnswers.get(0),
        securityQuestionAnswers.get(1),
        securityQuestionAnswers.get(2),
        securityQuestionAnswers.get(3)
    );

    try {
      final UserRegistrationController userRegistrationController = new UserRegistrationController();
      final boolean isUserRegistered = userRegistrationController.registerUser(user);
      if (isUserRegistered) {
        printer.printContent("User " + userName + " registered successfully!");
      } else {
        printer.printContent("User " + userName + " registration failed!");
      }
    } catch (final UserAuthenticationException e) {
      printer.printContent(e.toString());
    }
  }
}