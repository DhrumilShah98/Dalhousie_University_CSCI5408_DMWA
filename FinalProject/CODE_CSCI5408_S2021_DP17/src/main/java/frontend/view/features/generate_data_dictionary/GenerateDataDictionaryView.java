package frontend.view.features.generate_data_dictionary;

import backend.data_dictionary_generator.controller.DataDictionaryGeneratorController;
import backend.data_dictionary_generator.exception.DataDictionaryException;
import frontend.print.CSCI5408DP17Printer;
import frontend.session.Session;

import java.util.Scanner;

public final class GenerateDataDictionaryView {

  private final CSCI5408DP17Printer printer;
  private final Scanner scanner;
  private final Session userSession;

  public GenerateDataDictionaryView(final CSCI5408DP17Printer printer,
                                    final Scanner scanner,
                                    final Session userSession) {
    this.printer = printer;
    this.scanner = scanner;
    this.userSession = userSession;
  }

  public void generateDataDictionary() {
    printer.printScreenTitle("Generate Data Dictionary");
    while (true) {
      printer.printContent("1. Generate Data Dictionary.");
      printer.printContent("2. Go Back.");
      printer.printContent("Select an option:");
      final String input = scanner.nextLine();

      switch (input) {
        case "1":
          try {
            printer.printContent("Enter database name to generate Data Dictionary:");
            final String databaseName = scanner.nextLine().trim();
            final DataDictionaryGeneratorController dataDictionaryGeneratorController =
                new DataDictionaryGeneratorController();
            final boolean isDataDictionaryGenerated = dataDictionaryGeneratorController.generateDataDictionary(databaseName);
            if (isDataDictionaryGenerated) {
              printer.printContent("Data Dictionary generated for database: " + databaseName);
            }
          } catch (final DataDictionaryException e) {
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