package backend.sql_erd_generator.controller;

import backend.log_generator.controller.EventLogController;
import backend.log_generator.controller.GeneralLogController;
import backend.sql_erd_generator.exception.SQLERDException;
import backend.sql_erd_generator.util.ValidationUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static backend.sql_erd_generator.constant.Constant.*;

public final class SQLERDGeneratorController {

  private final GeneralLogController generalLogController;
  private final EventLogController eventLogController;

  public SQLERDGeneratorController() {
    this.generalLogController = new GeneralLogController();
    this.eventLogController = new EventLogController();
  }

  private String createCardinality(final String sourceTable,
                                   final String sourceColumn,
                                   final String destinationTable,
                                   final String destinationColumn) {
    return sourceTable + "." + sourceColumn +
        " -> " +
        destinationTable + "." + destinationColumn;
  }

  private void createSQLERD(final File[] allTables,
                            final String databaseName) throws SQLERDException {
    final String outputSQLERDFile = SQL_ERD_PATH + databaseName + "-" + System.currentTimeMillis() + ".txt";
    for (final File tableName : allTables) {
      try (final FileWriter sqlFileWriter = new FileWriter(outputSQLERDFile, true);
           final FileReader tableReader = new FileReader(tableName);
           final BufferedReader tableBufferedReader = new BufferedReader(tableReader)) {
        final String tableNameString = tableName.getName().split("\\.")[0];
        sqlFileWriter.append(tableNameString);
        sqlFileWriter.append("\n");
        StringBuilder underlineStringBuilder = new StringBuilder();
        for (int i = 0; i < tableNameString.length(); ++i) {
          underlineStringBuilder.append("-");
        }
        sqlFileWriter.append(underlineStringBuilder.toString());
        sqlFileWriter.append("\n");
        final String currentTableHeading = tableBufferedReader.readLine();
        final String[] rawTableHeadingColumns = currentTableHeading.split(DELIMITER);
        final List<String> cardinalities = new ArrayList<>();
        for (final String rawTableHeadingColumn : rawTableHeadingColumns) {
          final String[] rawTableHeadingColumnTokens = rawTableHeadingColumn.split("\\(");
          final String colName = rawTableHeadingColumnTokens[0];
          final String colAttributes = rawTableHeadingColumnTokens[1].substring(0, rawTableHeadingColumnTokens[1].length() - 1);
          final String[] colAttributesToken = colAttributes.split("\\|");
          if (colAttributesToken.length == 2 && colAttributesToken[1].equals(PRIMARY_KEY_CONSTRAINT)) {
            sqlFileWriter.append(PRIMARY_KEY_CONSTRAINT).append(" ")
                .append("|").append(" ")
                .append(colName).append(" ")
                .append(colAttributesToken[0]);
          } else if (colAttributesToken.length == 4 && colAttributesToken[1].equals(FOREIGN_KEY_CONSTRAINT)) {
            sqlFileWriter.append(FOREIGN_KEY_CONSTRAINT).append(" ")
                .append("|").append(" ")
                .append(colName).append(" ")
                .append(colAttributesToken[0]);
            cardinalities.add(createCardinality(tableNameString, colName, colAttributesToken[2], colAttributesToken[3]));
          } else {
            sqlFileWriter.append(colName).append(" ")
                .append(colAttributesToken[0]);
          }
          sqlFileWriter.append("\n");
        }
        sqlFileWriter.append("\n");
        for (final String cardinality : cardinalities) {
          sqlFileWriter.append(cardinality);
          sqlFileWriter.append("\n");
        }
        sqlFileWriter.append("\n");
      } catch (final IOException e) {
        final String message = "Error: {" + e.getMessage() + "}!";
        eventLogController.storeQueryLog(message, Instant.now());
        throw new SQLERDException(message);
      }
    }
  }

  private File[] readAllTables(final String databasePath) {
    final File allTables = new File(databasePath);
    return allTables.listFiles();
  }

  public boolean generateSQLERD(final String databaseName)
      throws SQLERDException {
    final boolean databaseNameValid = ValidationUtil.isDatabaseNameValid(databaseName);
    if (!databaseNameValid) {
      final String message = "Error: Failed to generate ERD for database " + databaseName + ". Invalid database name!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new SQLERDException(message);
    }
    final String databasePath = DATABASE_PATH + databaseName;
    if (!Files.exists(Paths.get(databasePath))) {
      final String message = "Error: Invalid database path " + databasePath + " or database does not exists!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new SQLERDException(message);
    }
    final File[] allTables = readAllTables(databasePath);
    createSQLERD(allTables, databaseName);
    generalLogController.storeQueryLog("ERD generated successfully for database " + databaseName + "!",
        Instant.now());
    return true;
  }
}