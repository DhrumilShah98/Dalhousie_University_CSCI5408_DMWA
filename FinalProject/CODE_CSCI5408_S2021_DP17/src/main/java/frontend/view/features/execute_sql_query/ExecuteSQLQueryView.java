package frontend.view.features.execute_sql_query;

import backend.query_parser.exception.QueryParserException;
import backend.query_processor.controller.QueryProcessorController;
import backend.query_processor.exception.QueryProcessorException;
import backend.query_processor.model.QueryProcessorResponse;
import frontend.print.CSCI5408DP17Printer;
import frontend.session.Session;

import java.util.Scanner;

public final class ExecuteSQLQueryView {

  private final CSCI5408DP17Printer printer;
  private final Scanner scanner;
  private final Session userSession;

  public ExecuteSQLQueryView(final CSCI5408DP17Printer printer,
                             final Scanner scanner,
                             final Session userSession) {
    this.printer = printer;
    this.scanner = scanner;
    this.userSession = userSession;
  }

  public void executeSQLQuery() {
    final QueryProcessorController queryProcessorController = new QueryProcessorController();
    printer.printScreenTitle("Execute SQL Query");
    while (true) {
      printer.printContent("1. Execute SQL Query.");
      printer.printContent("2. Go Back.");
      printer.printContent("Select an option:");
      final String input = scanner.nextLine();

      switch (input) {
        case "1":
          printer.printContent("Enter SQL Query:");
          final String sqlQuery = scanner.nextLine();
          try {
            final QueryProcessorResponse queryProcessorResponse = queryProcessorController.processQuery(sqlQuery);
            if (queryProcessorResponse.isSuccess()) {
              printer.printContent(queryProcessorResponse.getResult());
            }
          } catch (final QueryProcessorException | QueryParserException e) {
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