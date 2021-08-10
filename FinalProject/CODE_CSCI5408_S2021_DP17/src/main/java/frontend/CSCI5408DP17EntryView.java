package frontend;

import backend.authentication.model.User;
import frontend.print.CSCI5408DP17Printer;
import frontend.session.Session;
import frontend.view.UserLoginView;
import frontend.view.UserRegistrationView;
import frontend.view.features.MainMenuView;

import java.io.File;
import java.util.Scanner;

public final class CSCI5408DP17EntryView {

  private void folderStructureSetup(final CSCI5408DP17Printer printer) {
    final File outputFolder = new File("./src/main/java/output");
    final File dataDictionaryFolder = new File(outputFolder + "/data_dictionary");
    final File erdFilesFolder = new File(outputFolder + "/erd_files");
    final File logsFolder = new File(outputFolder + "/logs");
    final File metaDataFolder = new File(outputFolder + "/metadata");
    final File sqlDumpsFolder = new File(outputFolder + "/sql_dumps");
    final File databaseServerFolder = new File("./src/main/java/database/database_server");
    final File databaseInMemoryFolder = new File("./src/main/java/database/database_in_memory");
    if (!outputFolder.exists()) {
      if (outputFolder.mkdirs()) {
        printer.printContent(outputFolder.getName() + " directory created!");
      }
    }
    if (!dataDictionaryFolder.exists()) {
      if (dataDictionaryFolder.mkdirs()) {
        printer.printContent(dataDictionaryFolder.getName() + " directory created!");
      }
    }
    if (!erdFilesFolder.exists()) {
      if (erdFilesFolder.mkdirs()) {
        printer.printContent(erdFilesFolder.getName() + " directory created!");
      }
    }
    if (!logsFolder.exists()) {
      if (logsFolder.mkdirs()) {
        printer.printContent(logsFolder.getName() + " directory created!");
      }
    }
    if (!metaDataFolder.exists()) {
      if (metaDataFolder.mkdirs()) {
        printer.printContent(metaDataFolder.getName() + " directory created!");
      }
    }
    if (!sqlDumpsFolder.exists()) {
      if (sqlDumpsFolder.mkdirs()) {
        printer.printContent(sqlDumpsFolder.getName() + " directory created!");
      }
    }
    if (!databaseServerFolder.exists()) {
      if (databaseServerFolder.mkdirs()) {
        printer.printContent(sqlDumpsFolder.getName() + " directory created!");
      }
    }
    if (!databaseInMemoryFolder.exists()) {
      if (databaseInMemoryFolder.mkdirs()) {
        printer.printContent(databaseInMemoryFolder.getName() + " directory created!");
      }
    }
  }

  private void userRegistration(final CSCI5408DP17Printer printer,
                                final Scanner scanner) {
    final UserRegistrationView userRegistrationView =
        new UserRegistrationView(printer, scanner);
    userRegistrationView.performUserRegistration();
  }

  private User userLogin(final CSCI5408DP17Printer printer,
                         final Scanner scanner) {
    final UserLoginView userLoginView =
        new UserLoginView(printer, scanner);
    return userLoginView.performLogin();
  }

  public static void main(String[] args) {
    final CSCI5408DP17EntryView entry = new CSCI5408DP17EntryView();
    final CSCI5408DP17Printer printer = CSCI5408DP17Printer.getInstance();
    final Scanner scanner = new Scanner(System.in);
    final Session userSession = Session.getInstance();
    entry.folderStructureSetup(printer);

    printer.printScreenTitle("Welcome to CSCI5408 DP17 Project");

    while (true) {
      printer.printContent("1. User registration.");
      printer.printContent("2. User login.");
      printer.printContent("3. Exit.");
      printer.printContent("Select an option:");
      final String input = scanner.nextLine();

      switch (input) {
        case "1":
          entry.userRegistration(printer, scanner);
          break;
        case "2":
          final User user = entry.userLogin(printer, scanner);
          if (user != null) {
            userSession.createUserSession(user);
            final MainMenuView mainMenuView = new MainMenuView(printer, scanner, userSession);
            mainMenuView.displayMainMenu();
          }
          break;
        case "3":
          userSession.destroyUserSession();
          System.exit(0);
        default:
          break;
      }
    }
  }
}