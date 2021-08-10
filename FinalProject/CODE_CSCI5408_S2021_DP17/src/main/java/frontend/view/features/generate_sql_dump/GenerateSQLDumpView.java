package frontend.view.features.generate_sql_dump;

import backend.sql_dump_generator.controller.SQLDumpGeneratorController;
import backend.sql_dump_generator.exception.SQLDumpException;
import frontend.print.CSCI5408DP17Printer;
import frontend.session.Session;

import java.util.Scanner;

public final class GenerateSQLDumpView {

  private final CSCI5408DP17Printer printer;
  private final Scanner scanner;
  private final Session userSession;

  public GenerateSQLDumpView(final CSCI5408DP17Printer printer,
                             final Scanner scanner,
                             final Session userSession) {
    this.printer = printer;
    this.scanner = scanner;
    this.userSession = userSession;
  }

  public void generateSQLDump() {
    printer.printScreenTitle("Generate SQL Dump");
    while (true) {
      printer.printContent("1. Generate SQL Dump.");
      printer.printContent("2. Go Back.");
      printer.printContent("Select an option:");
      final String input = scanner.nextLine();

      switch (input) {
        case "1":
          try {
            printer.printContent("Enter database name to generate SQL dump:");
            final String databaseName = scanner.nextLine().trim();
            final SQLDumpGeneratorController sqlDumpGeneratorController =
                new SQLDumpGeneratorController();
            final boolean isSQLDumpGenerated = sqlDumpGeneratorController.generateSQLDump(databaseName);
            if (isSQLDumpGenerated) {
              printer.printContent("SQL Dump generated for database: " + databaseName);
            }
          } catch (final SQLDumpException e) {
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