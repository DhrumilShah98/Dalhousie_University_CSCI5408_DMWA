package frontend.view.features.generate_erd;

import backend.sql_erd_generator.controller.SQLERDGeneratorController;
import backend.sql_erd_generator.exception.SQLERDException;
import frontend.print.CSCI5408DP17Printer;
import frontend.session.Session;

import java.util.Scanner;

public final class GenerateERDView {

  private final CSCI5408DP17Printer printer;
  private final Scanner scanner;
  private final Session userSession;

  public GenerateERDView(final CSCI5408DP17Printer printer,
                         final Scanner scanner,
                         final Session userSession) {
    this.printer = printer;
    this.scanner = scanner;
    this.userSession = userSession;
  }

  public void generateERD() {
    printer.printScreenTitle("Generate ERD");
    while (true) {
      printer.printContent("1. Generate ERD.");
      printer.printContent("2. Go Back.");
      printer.printContent("Select an option:");
      final String input = scanner.nextLine();

      switch (input) {
        case "1":
          try {
            printer.printContent("Enter database name to generate ERD diagram:");
            final String databaseName = scanner.nextLine().trim();
            final SQLERDGeneratorController sqlERDGeneratorController =
                new SQLERDGeneratorController();
            final boolean isSQLERDGenerated = sqlERDGeneratorController.generateSQLERD(databaseName);
            if (isSQLERDGenerated) {
              printer.printContent("SQL ERD generated for database: " + databaseName);
            }
          } catch (final SQLERDException e) {
            printer.printContent(e.toString());
          }
          break;
        case "2":
          return;
        default:
          break;
      }
    }
  }
}