package backend.sql_dump_generator.controller;

import backend.log_generator.controller.EventLogController;
import backend.log_generator.controller.GeneralLogController;
import backend.sql_dump_generator.exception.SQLDumpException;
import backend.sql_dump_generator.util.ValidationUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

import static backend.sql_dump_generator.constant.Constant.*;

public final class SQLDumpGeneratorController {

  private final GeneralLogController generalLogController;
  private final EventLogController eventLogController;

  public SQLDumpGeneratorController() {
    this.generalLogController = new GeneralLogController();
    this.eventLogController = new EventLogController();
  }

  private String getCreateTableQuery(final String tableHeading,
                                     final String tableName) {
    final StringBuilder createStringBuilder = new StringBuilder();
    final String[] rawTableHeadingColumns = tableHeading.split(DELIMITER);
    createStringBuilder.append("CREATE")
        .append(" ").append("TABLE")
        .append(" ").append(tableName)
        .append(" ").append("(");
    for (final String rawTableHeadingColumn : rawTableHeadingColumns) {
      final String[] rawTableHeadingColumnTokens = rawTableHeadingColumn.split("\\(");
      final String colName = rawTableHeadingColumnTokens[0];
      createStringBuilder.append(colName);
      final String colAttributes = rawTableHeadingColumnTokens[1].substring(0, rawTableHeadingColumnTokens[1].length() - 1);
      final String[] colAttributesToken = colAttributes.split("\\|");
      if (colAttributesToken.length == 2 && colAttributesToken[1].equals(PRIMARY_KEY_CONSTRAINT)) {
        createStringBuilder.append(" ").append(colAttributesToken[0])
            .append(" ").append("PRIMARY KEY")
            .append(", ");
      } else if (colAttributesToken.length == 4 && colAttributesToken[1].equals(FOREIGN_KEY_CONSTRAINT)) {
        createStringBuilder.append(" ").append(colAttributesToken[0])
            .append(" ").append("REFERENCES")
            .append(" ").append(colAttributesToken[2])
            .append("(").append(colAttributesToken[3]).append(")")
            .append(", ");
      } else {
        createStringBuilder.append(" ").append(colAttributesToken[0])
            .append(", ");
      }
    }
    createStringBuilder.replace(createStringBuilder.length() - 2, createStringBuilder.length(), "");
    createStringBuilder.append(");");
    return createStringBuilder.toString();
  }

  private String[] getTableColumnNames(final String tableHeading) {
    final String[] rawTableHeadingColumns = tableHeading.split(DELIMITER);
    final String[] tableColumnNames = new String[rawTableHeadingColumns.length];
    for (int i = 0; i < rawTableHeadingColumns.length; ++i) {
      final String[] rowColTokens = rawTableHeadingColumns[i].split("\\(");
      tableColumnNames[i] = rowColTokens[0];
    }
    return tableColumnNames;
  }

  private String[] getTableColumnDataTypes(final String tableHeading) {
    final String[] rawTableHeadingColumns = tableHeading.split(DELIMITER);
    final String[] tableColumnDataTypes = new String[rawTableHeadingColumns.length];
    for (int i = 0; i < rawTableHeadingColumns.length; ++i) {
      final String[] rowColTokens = rawTableHeadingColumns[i].split("\\(");
      final String colAttributes = rowColTokens[1].substring(0, rowColTokens[1].length() - 1);
      tableColumnDataTypes[i] = colAttributes.split("\\|")[0];
    }
    return tableColumnDataTypes;
  }

  private String getInsertIntoTableQuery(final String tableContent,
                                         final String tableName,
                                         final String[] tableColumnNames,
                                         final String[] tableColumnDataTypes) {
    final StringBuilder insertStringBuilder = new StringBuilder();
    insertStringBuilder.append("INSERT").append(" ").append("INTO").append(" ").append(tableName).append(" ").append("(");
    for (final String tableColumnName : tableColumnNames) {
      insertStringBuilder.append(tableColumnName).append(", ");
    }
    insertStringBuilder.replace(insertStringBuilder.length() - 2, insertStringBuilder.length(), "");
    insertStringBuilder.append(")").append(" ").append("VALUES").append(" ").append("(");
    final String[] rawTableContentColumns = tableContent.split(DELIMITER);
    for (int i = 0; i < rawTableContentColumns.length; ++i) {
      if (tableColumnDataTypes[i].equalsIgnoreCase(INT_DATATYPE)) {
        insertStringBuilder.append(rawTableContentColumns[i]).append(", ");
      }
      if (tableColumnDataTypes[i].equalsIgnoreCase(FLOAT_DATATYPE)) {
        insertStringBuilder.append(rawTableContentColumns[i]).append(", ");
      }
      if (tableColumnDataTypes[i].equalsIgnoreCase(BOOLEAN_DATATYPE)) {
        insertStringBuilder.append(rawTableContentColumns[i]).append(", ");
      }
      if (tableColumnDataTypes[i].equalsIgnoreCase(TEXT_DATATYPE)) {
        insertStringBuilder.append("\"").append(rawTableContentColumns[i]).append("\"").append(", ");
      }
    }
    insertStringBuilder.replace(insertStringBuilder.length() - 2, insertStringBuilder.length(), "");
    insertStringBuilder.append(");");
    return insertStringBuilder.toString();
  }

  private void createSQLDump(final File[] allTables,
                             final String databaseName) throws SQLDumpException {
    final String outputSQLDumpFile = SQL_DUMPS_PATH + databaseName + "-" + System.currentTimeMillis() + ".sql";
    for (final File tableName : allTables) {
      try (final FileWriter sqlFileWriter = new FileWriter(outputSQLDumpFile, true);
           final FileReader tableReader = new FileReader(tableName);
           final BufferedReader tableBufferedReader = new BufferedReader(tableReader)) {
        final String tableNameString = tableName.getName().split("\\.")[0];
        String currentTableEntry;
        boolean isHeading = true;
        String[] tableColumnNames = new String[0];
        String[] tableColumnDataTypes = new String[0];
        while ((currentTableEntry = tableBufferedReader.readLine()) != null) {
          if (isHeading) {
            final String createTableQuery = getCreateTableQuery(currentTableEntry, tableNameString);
            tableColumnNames = getTableColumnNames(currentTableEntry);
            tableColumnDataTypes = getTableColumnDataTypes(currentTableEntry);
            sqlFileWriter.append(createTableQuery);
            sqlFileWriter.append("\n");
            isHeading = false;
          } else {
            final String insertIntoTableQuery =
                getInsertIntoTableQuery(currentTableEntry, tableNameString, tableColumnNames, tableColumnDataTypes);
            sqlFileWriter.append(insertIntoTableQuery);
            sqlFileWriter.append("\n");
          }
        }
      } catch (final IOException e) {
        final String message = "Error: {" + e.getMessage() + "}!";
        eventLogController.storeQueryLog(message, Instant.now());
        throw new SQLDumpException(e.getMessage());
      }
    }
  }

  private File[] readAllTables(final String databasePath) {
    final File allTables = new File(databasePath);
    return allTables.listFiles();
  }

  public boolean generateSQLDump(final String databaseName)
      throws SQLDumpException {
    final boolean databaseNameValid = ValidationUtil.isDatabaseNameValid(databaseName);
    if (!databaseNameValid) {
      final String message = "Error: Failed to generate SQL Dump for database " + databaseName + ". Invalid database name!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new SQLDumpException(message);
    }
    final String databasePath = DATABASE_PATH + databaseName;
    if (!Files.exists(Paths.get(databasePath))) {
      final String message = "Error: Invalid database path " + databasePath + " or database does not exists!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new SQLDumpException(message);
    }
    final File[] allTables = readAllTables(databasePath);
    createSQLDump(allTables, databaseName);
    generalLogController.storeQueryLog("SQL Dump generated successfully for database " + databaseName + "!",
        Instant.now());
    return true;
  }
}