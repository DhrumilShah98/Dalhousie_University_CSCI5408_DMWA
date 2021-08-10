package backend.meta_data_generator.controller;

import backend.log_generator.controller.EventLogController;
import backend.log_generator.controller.GeneralLogController;
import backend.meta_data_generator.util.ValidationUtil;
import backend.meta_data_generator.exception.MetaDataException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

import static backend.meta_data_generator.constant.Constant.*;

public final class MetaDataGeneratorController {

  private final GeneralLogController generalLogController;
  private final EventLogController eventLogController;

  public MetaDataGeneratorController() {
    this.generalLogController = new GeneralLogController();
    this.eventLogController = new EventLogController();
  }

  private String createMetaData(final String databaseName,
                                final String tableName)
      throws MetaDataException {
    final StringBuilder metaDataStringBuilder = new StringBuilder();
    metaDataStringBuilder.append("Database").append(": ").append(databaseName).append("\n");
    metaDataStringBuilder.append("Table").append(": ").append(tableName).append("\n");
    metaDataStringBuilder.append("Columns").append(":").append("\n");
    final String tablePath = DATABASE_PATH + databaseName + "/" + tableName + ".txt";
    try (final FileReader tableReader = new FileReader(tablePath);
         final BufferedReader tableBufferedReader = new BufferedReader(tableReader)) {
      final String currentTableHeading = tableBufferedReader.readLine();
      final String[] rawTableHeadingColumns = currentTableHeading.split(DELIMITER);
      for (int i = 0; i < rawTableHeadingColumns.length; i++) {
        final String rawTableHeadingColumn = rawTableHeadingColumns[i];
        final String[] rawTableHeadingColumnTokens = rawTableHeadingColumn.split("\\(");
        final String colName = rawTableHeadingColumnTokens[0];
        metaDataStringBuilder.append("\t").append("Column").append(" ").append(i + 1).append(": ").append(colName).append(" (");
        final String colAttributes = rawTableHeadingColumnTokens[1].substring(0, rawTableHeadingColumnTokens[1].length() - 1);
        final String[] colAttributesToken = colAttributes.split("\\|");
        metaDataStringBuilder.append("Datatype").append(": ").append(colAttributesToken[0]);
        if (colAttributesToken.length == 2 && colAttributesToken[1].equals(PRIMARY_KEY_CONSTRAINT)) {
          metaDataStringBuilder.append(" | ").append("Key").append(": ").append("PRIMARY KEY");
        }
        if (colAttributesToken.length == 4 && colAttributesToken[1].equals(FOREIGN_KEY_CONSTRAINT)) {
          metaDataStringBuilder.append(" | ").append("Key").append(": ").append("FOREIGN KEY");
          metaDataStringBuilder.append(" | ").append("Reference Table").append(": ").append(colAttributesToken[2]);
          metaDataStringBuilder.append(" | ").append("Reference Column").append(": ").append(colAttributesToken[3]);
        }
        metaDataStringBuilder.append(")").append("\n");
      }
    } catch (final IOException e) {
      final String message = "Error: {" + e.getMessage() + "}!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new MetaDataException(message);
    }
    return metaDataStringBuilder.toString();
  }

  private void storeMetaData(final String metaDataDatabasePath,
                             final String databaseName,
                             final String tableName)
      throws MetaDataException {
    final String metaDataTablePath = metaDataDatabasePath + "/" + tableName + ".txt";
    try (final FileWriter metaDataFileWriter = new FileWriter(metaDataTablePath, true)) {
      metaDataFileWriter.append(createMetaData(databaseName, tableName));
      final String message = "Metadata stored successfully for database " + databaseName +
          " and table " + tableName + "!";
      generalLogController.storeQueryLog(message, Instant.now());
    } catch (final IOException e) {
      final String message = "Error: {" + e.getMessage() + "}!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new MetaDataException(message);
    }
  }

  private void validate(final String databaseName,
                        final String tableName)
      throws MetaDataException {
    final boolean databaseNameValid = ValidationUtil.isDatabaseNameValid(databaseName);
    if (!databaseNameValid) {
      final String message = "Error: Failed to generate Metadata for database " + databaseName +
          " and table " + tableName + ". Invalid database name!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new MetaDataException(message);
    }
    final boolean tableNameValid = ValidationUtil.isTableNameValid(tableName);
    if (!tableNameValid) {
      final String message = "Error: Failed to generate Metadata for database " + databaseName +
          " and table " + tableName + ". Invalid table name!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new MetaDataException(message);
    }
    final String databasePath = DATABASE_PATH + databaseName;
    if (!Files.exists(Paths.get(databasePath))) {
      final String message = "Error: Invalid database path " + databasePath +
          " or database does not exists!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new MetaDataException(message);
    }
    final String tablePath = DATABASE_PATH + databaseName + "/" + tableName + ".txt";
    if (!Files.exists(Paths.get(tablePath))) {
      final String message = "Error: Invalid table path " + tablePath + " or database does not exists!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new MetaDataException(message);
    }
  }

  public void generateMetaData(final String databaseName,
                               final String tableName)
      throws MetaDataException {
    validate(databaseName, tableName);
    final String metaDataDatabasePath = META_DATA_PATH + databaseName;
    final File metadataDataBase = new File(metaDataDatabasePath);
    if (!metadataDataBase.exists() && !metadataDataBase.isDirectory()) {
      final boolean metaDataDatabaseDir = metadataDataBase.mkdir();
      if (metaDataDatabaseDir) {
        storeMetaData(metaDataDatabasePath, databaseName, tableName);
      } else {
        final String message = "Error: Error creating metadata database!";
        eventLogController.storeQueryLog(message, Instant.now());
        throw new MetaDataException(message);
      }
    } else {
      storeMetaData(metaDataDatabasePath, databaseName, tableName);
    }
  }

  public String viewMetaData(final String databaseName,
                             final String tableName)
      throws MetaDataException {
    validate(databaseName, tableName);
    final String metaDataDatabasePath = META_DATA_PATH + databaseName;
    final String metaDataTablePath = metaDataDatabasePath + "/" + tableName + ".txt";
    final File metaDataFile = new File(metaDataTablePath);
    if (metaDataFile.exists() && metaDataFile.isFile()) {
      try (final BufferedReader metaDataBufferReader = new BufferedReader(new FileReader(metaDataFile))) {
        final StringBuilder metaDataBuilder = new StringBuilder();
        String line;
        while ((line = metaDataBufferReader.readLine()) != null) {
          metaDataBuilder.append(line).append("\n");
        }
        generalLogController.storeQueryLog("View metadata request for database " + databaseName +
            " and table " + tableName + "!", Instant.now());
        return metaDataBuilder.toString();
      } catch (final IOException e) {
        final String message = "Error: {" + e.getMessage() + "}!";
        eventLogController.storeQueryLog(message, Instant.now());
        return createMetaData(databaseName, tableName);
      }
    } else {
      generateMetaData(databaseName, tableName);
      generalLogController.storeQueryLog("Metadata generated successfully for database " + databaseName +
          " and table " + tableName + "!", Instant.now());
      return createMetaData(databaseName, tableName);
    }
  }
}