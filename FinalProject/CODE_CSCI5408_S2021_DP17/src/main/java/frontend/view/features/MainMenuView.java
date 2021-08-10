package frontend.view.features;

import frontend.print.CSCI5408DP17Printer;
import frontend.session.Session;
import frontend.view.features.execute_sql_query.ExecuteSQLQueryView;
import frontend.view.features.generate_data_dictionary.GenerateDataDictionaryView;
import frontend.view.features.generate_erd.GenerateERDView;
import frontend.view.features.generate_meta_data.GenerateMetaDataView;
import frontend.view.features.generate_sql_dump.GenerateSQLDumpView;
import frontend.view.features.import_sql_dump.ImportSQLDumpView;

import java.util.Scanner;

public final class MainMenuView {

  private final CSCI5408DP17Printer printer;
  private final Scanner scanner;
  private final Session userSession;

  public MainMenuView(final CSCI5408DP17Printer printer,
                      final Scanner scanner,
                      final Session userSession) {
    this.printer = printer;
    this.scanner = scanner;
    this.userSession = userSession;
  }

  public void displayMainMenu() {
    printer.printScreenTitle("Main Menu");
    while (true) {
      printer.printContent("1. Execute SQL Query.");
      printer.printContent("2. Generate SQL Dump.");
      printer.printContent("3. Generate ERD.");
      printer.printContent("4. Generate Data Dictionary.");
      printer.printContent("5. View Meta Data.");
      printer.printContent("6. Import SQL Dump.");
      printer.printContent("7. Logout.");
      printer.printContent("Select an option:");
      final String input = scanner.nextLine();

      switch (input) {
        case "1":
          final ExecuteSQLQueryView executeSQLQueryView =
              new ExecuteSQLQueryView(printer, scanner, userSession);
          executeSQLQueryView.executeSQLQuery();
          break;
        case "2":
          final GenerateSQLDumpView generateSQLDumpView =
              new GenerateSQLDumpView(printer, scanner, userSession);
          generateSQLDumpView.generateSQLDump();
          break;
        case "3":
          final GenerateERDView generateERDView =
              new GenerateERDView(printer, scanner, userSession);
          generateERDView.generateERD();
          break;
        case "4":
          final GenerateDataDictionaryView generateDataDictionaryView =
              new GenerateDataDictionaryView(printer, scanner, userSession);
          generateDataDictionaryView.generateDataDictionary();
          break;
        case "5":
          final GenerateMetaDataView generateMetaDataView =
              new GenerateMetaDataView(printer, scanner, userSession);
          generateMetaDataView.generateMetaData();
          break;
        case "6":
          final ImportSQLDumpView importSQLDumpView =
              new ImportSQLDumpView(printer, scanner, userSession);
          importSQLDumpView.importSQLDump();
          break;
        case "7":
          userSession.destroyUserSession();
          return;
        default:
          break;
      }
    }
  }
}