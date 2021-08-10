package backend.data_dictionary_generator.controller;

import backend.data_dictionary_generator.exception.DataDictionaryException;
import backend.data_dictionary_generator.util.ValidationUtil;
import backend.log_generator.controller.EventLogController;
import backend.log_generator.controller.GeneralLogController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

import static backend.data_dictionary_generator.constant.Constant.*;

public final class DataDictionaryGeneratorController {

  private final GeneralLogController generalLogController;
  private final EventLogController eventLogController;

  public DataDictionaryGeneratorController() {
    this.generalLogController = new GeneralLogController();
    this.eventLogController = new EventLogController();
  }

  private void createDataDictionary(final File[] allTables,
                                    final String databaseName)
      throws DataDictionaryException {
    final StringBuilder dataDictionaryStringBuilder = new StringBuilder();
    dataDictionaryStringBuilder.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>").append("\n");
    dataDictionaryStringBuilder.append("<database").append(" ")
        .append("name").append("=").append("\"").append(databaseName).append("\"")
        .append(">").append("\n");
    dataDictionaryStringBuilder.append("\t").append("<tables>").append("\n");
    for (final File tableName : allTables) {
      try (final FileReader tableReader = new FileReader(tableName);
           final BufferedReader tableBufferedReader = new BufferedReader(tableReader)) {
        final String tableNameString = tableName.getName().split("\\.")[0];
        dataDictionaryStringBuilder.append("\t\t").append("<table").append(" ")
            .append("name").append("=").append("\"").append(tableNameString).append("\"")
            .append(">").append("\n");
        dataDictionaryStringBuilder.append("\t\t\t").append("<columns>").append("\n");
        final String currentTableHeading = tableBufferedReader.readLine();
        final String[] rawTableHeadingColumns = currentTableHeading.split(DELIMITER);
        for (int i = 0; i < rawTableHeadingColumns.length; i++) {
          final String rawTableHeadingColumn = rawTableHeadingColumns[i];
          final String[] rawTableHeadingColumnTokens = rawTableHeadingColumn.split("\\(");
          final String colName = rawTableHeadingColumnTokens[0];
          dataDictionaryStringBuilder.append("\t\t\t\t").append("<column").append(" ")
              .append("name").append("=").append("\"").append(colName).append("\"")
              .append(">").append("\n");
          dataDictionaryStringBuilder.append("\t\t\t\t\t").append("<columnId>").append(i).append("</columnId>").append("\n");
          final String colAttributes = rawTableHeadingColumnTokens[1].substring(0, rawTableHeadingColumnTokens[1].length() - 1);
          final String[] colAttributesToken = colAttributes.split("\\|");
          dataDictionaryStringBuilder.append("\t\t\t\t\t").append("<datatype>").append(colAttributesToken[0]).append("</datatype>").append("\n");
          if (colAttributesToken.length == 2 && colAttributesToken[1].equals(PRIMARY_KEY_CONSTRAINT)) {
            dataDictionaryStringBuilder.append("\t\t\t\t\t").append("<key>").append("PRIMARY KEY").append("</key>").append("\n");
          }
          if (colAttributesToken.length == 4 && colAttributesToken[1].equals(FOREIGN_KEY_CONSTRAINT)) {
            dataDictionaryStringBuilder.append("\t\t\t\t\t").append("<key>").append("FOREIGN KEY").append("</key>").append("\n");
            dataDictionaryStringBuilder.append("\t\t\t\t\t").append("<reference_table>").append(colAttributesToken[2]).append("</reference_table>").append("\n");
            dataDictionaryStringBuilder.append("\t\t\t\t\t").append("<reference_column>").append(colAttributesToken[3]).append("</reference_column>").append("\n");
          }
          dataDictionaryStringBuilder.append("\t\t\t\t").append("</column>").append("\n");
        }
        dataDictionaryStringBuilder.append("\t\t\t").append("</columns>").append("\n");
        dataDictionaryStringBuilder.append("\t\t").append("</table>").append("\n");
      } catch (final IOException e) {
        final String message = "Error: {" + e.getMessage() + "}!";
        eventLogController.storeQueryLog(message, Instant.now());
        throw new DataDictionaryException(message);
      }
    }
    dataDictionaryStringBuilder.append("\t").append("</tables>").append("\n");
    dataDictionaryStringBuilder.append("</database>");
    final String outputDataDictionaryFile = DATA_DICTIONARY_PATH + databaseName + "-" + System.currentTimeMillis() + ".xml";
    try (final FileWriter sqlFileWriter = new FileWriter(outputDataDictionaryFile, true)) {
      sqlFileWriter.append(dataDictionaryStringBuilder.toString());
    } catch (final IOException e) {
      final String message = "Error: {" + e.getMessage() + "}!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new DataDictionaryException(message);
    }
  }

  private File[] readAllTables(final String databasePath) {
    final File allTables = new File(databasePath);
    return allTables.listFiles();
  }

  public boolean generateDataDictionary(final String databaseName)
      throws DataDictionaryException {
    final boolean databaseNameValid = ValidationUtil.isDatabaseNameValid(databaseName);
    if (!databaseNameValid) {
      final String message = "Error: Failed to generate Data Dictionary for database " + databaseName + ". Invalid database name!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new DataDictionaryException(message);
    }
    final String databasePath = DATABASE_PATH + databaseName;
    if (!Files.exists(Paths.get(databasePath))) {
      final String message = "Error: Invalid database path " + databasePath + " or database does not exists!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new DataDictionaryException(message);
    }
    final File[] allTables = readAllTables(databasePath);
    createDataDictionary(allTables, databaseName);
    generalLogController.storeQueryLog("Data Dictionary generated successfully for database " + databaseName + "!",
        Instant.now());
    return true;
  }
}