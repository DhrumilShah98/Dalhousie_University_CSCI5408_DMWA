package frontend.view.features.import_sql_dump;

import backend.sql_dump_import.controller.SQLDumpImportController;
import backend.sql_dump_import.exception.SQLDumpImportException;
import frontend.print.CSCI5408DP17Printer;
import frontend.session.Session;

import java.util.Scanner;

public final class ImportSQLDumpView {

  private final CSCI5408DP17Printer printer;
  private final Scanner scanner;
  private final Session userSession;

  public ImportSQLDumpView(final CSCI5408DP17Printer printer,
                           final Scanner scanner,
                           final Session userSession) {
    this.printer = printer;
    this.scanner = scanner;
    this.userSession = userSession;
  }

  public void importSQLDump() {
    printer.printScreenTitle("Import SQL Dump");
    while (true) {
      printer.printContent("1. Import SQL Dump.");
      printer.printContent("2. Go Back.");
      printer.printContent("Select an option:");
      final String input = scanner.nextLine();

      switch (input) {
        case "1":
          try {
            printer.printContent("Enter file name to import SQL dump: (.sql extension)");
            final String databaseName = scanner.nextLine().trim();
            final SQLDumpImportController sqlDumpImportController =
                new SQLDumpImportController();
            final boolean isSQLDumpImported = sqlDumpImportController.importSQLDump(databaseName);
            if (isSQLDumpImported) {
              printer.printContent("SQL Dump imported to database: " + databaseName);
            }
          } catch (final SQLDumpImportException e) {
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
