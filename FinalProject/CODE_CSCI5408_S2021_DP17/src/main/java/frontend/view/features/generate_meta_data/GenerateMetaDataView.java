package frontend.view.features.generate_meta_data;

import backend.meta_data_generator.controller.MetaDataGeneratorController;
import backend.meta_data_generator.exception.MetaDataException;
import frontend.print.CSCI5408DP17Printer;
import frontend.session.Session;

import java.util.Scanner;

public final class GenerateMetaDataView {

  private final CSCI5408DP17Printer printer;
  private final Scanner scanner;
  private final Session userSession;

  public GenerateMetaDataView(final CSCI5408DP17Printer printer,
                              final Scanner scanner,
                              final Session userSession) {
    this.printer = printer;
    this.scanner = scanner;
    this.userSession = userSession;
  }

  public void generateMetaData() {
    printer.printScreenTitle("View Meta Data");
    while (true) {
      printer.printContent("1. View Meta Data.");
      printer.printContent("2. Go Back.");
      printer.printContent("Select an option:");
      final String input = scanner.nextLine();

      switch (input) {
        case "1":
          try {
            printer.printContent("Enter database name to generate Meta Data:");
            final String databaseName = scanner.nextLine().trim();
            printer.printContent("Enter table name to generate Meta Data:");
            final String tableName = scanner.nextLine().trim();
            final MetaDataGeneratorController metaDataGeneratorController =
                new MetaDataGeneratorController();
            final String metaData = metaDataGeneratorController.viewMetaData(databaseName, tableName);
            printer.printContent(metaData);
          } catch (final MetaDataException e) {
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