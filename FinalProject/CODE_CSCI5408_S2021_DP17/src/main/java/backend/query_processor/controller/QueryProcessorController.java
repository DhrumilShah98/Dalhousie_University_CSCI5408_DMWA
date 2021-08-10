package backend.query_processor.controller;

import backend.log_generator.controller.EventLogController;
import backend.log_generator.controller.GeneralLogController;
import backend.log_generator.controller.QueryLogController;
import backend.meta_data_generator.controller.MetaDataGeneratorController;
import backend.meta_data_generator.exception.MetaDataException;
import backend.query_parser.controller.QueryParserController;
import backend.query_parser.exception.QueryParserException;
import backend.query_processor.exception.QueryProcessorException;
import backend.query_processor.lock.Lock;
import backend.query_processor.model.QueryProcessorResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.time.Instant;

import static backend.query_processor.constant.Constant.*;
import static backend.query_parser.constant.Constant.*;

public final class QueryProcessorController {

  private final QueryParserController queryParserController;
  private final MetaDataGeneratorController metaDataGeneratorController;
  private final QueryLogController queryLogController;
  private final GeneralLogController generalLogController;
  private final EventLogController eventLogController;
  private String useDatabaseName = null;

  public QueryProcessorController() {
    this.queryParserController = new QueryParserController();
    this.metaDataGeneratorController = new MetaDataGeneratorController();
    this.queryLogController = new QueryLogController();
    this.generalLogController = new GeneralLogController();
    this.eventLogController = new EventLogController();
  }

  private boolean isDatabaseSelected() {
    return this.useDatabaseName != null && !this.useDatabaseName.isEmpty();
  }

  private long getQueryExecutionTime(final Instant startTime,
                                     final Instant endTime) {
    final Duration timeElapsed = Duration.between(startTime, endTime);
    return timeElapsed.toMillis();
  }

  private QueryProcessorResponse processCreateDatabaseQuery(final String query)
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    final String queryProcessed = query.substring(0, query.length() - 1);
    final String[] temporaryArray = queryProcessed.split(" ");
    final String databaseName = temporaryArray[2];
    final String databasePath = DATABASE_PATH + databaseName;
    final File database = new File(databasePath);
    final boolean isDatabaseExists = database.isDirectory();
    if (isDatabaseExists) {
      final String message = "Error: Database " + databaseName + " already exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    Lock.applyExclusiveLock(databaseName, null); // Apply Exclusive Lock
    final boolean isDirectoryCreated = database.mkdir();
    Lock.releaseExclusiveLock(databaseName, null); // Release Exclusive Lock
    if (isDirectoryCreated) {
      final String message = "Database " + databaseName + " created successfully!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      generalLogController.storeQueryLog(message, Instant.now());
      return new QueryProcessorResponse(true, message);
    } else {
      final String message = "Error: Database " + databaseName + " creation error!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
  }

  private QueryProcessorResponse processDropDatabaseQuery(final String query)
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    final String queryProcessed = query.substring(0, query.length() - 1);
    final String[] temporaryArray = queryProcessed.split(" ");
    final String databaseName = temporaryArray[2];
    final String databasePath = DATABASE_PATH + databaseName;
    final File database = new File(databasePath);
    final boolean isDatabaseExists = database.isDirectory();
    if (!isDatabaseExists) {
      final String message = "Error: Database " + databaseName + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    Lock.applyExclusiveLock(databaseName, null); // Apply Exclusive Lock
    final File[] allTables = database.listFiles();
    if (allTables == null) {
      Lock.releaseExclusiveLock(databaseName, null); // Release Exclusive Lock
      final String message = "Error: Database " + databaseName + " failed to delete!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    for (final File table : allTables) {
      final boolean isTableDeleted = table.delete();
      if (!isTableDeleted) {
        Lock.releaseExclusiveLock(databaseName, null); // Release Exclusive Lock
        final String message = "Error: Failed to delete tables of the database " + databaseName + " Please try again!" + " | " +
            "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
        eventLogController.storeQueryLog(message, Instant.now());
        throw new QueryProcessorException(message);
      }
    }
    final boolean isDatabaseDeleted = database.delete();
    if (isDatabaseDeleted) {
      this.useDatabaseName = null;
      Lock.releaseExclusiveLock(databaseName, null); // Release Exclusive Lock
      final String message = "Database " + databaseName + " dropped successfully!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      generalLogController.storeQueryLog(message, Instant.now());
      return new QueryProcessorResponse(true, message);
    } else {
      Lock.releaseExclusiveLock(databaseName, null); // Release Exclusive Lock
      final String message = "Error: Database " + databaseName + " deletion error!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
  }

  private QueryProcessorResponse processUseDatabaseQuery(final String query)
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    final String queryProcessed = query.substring(0, query.length() - 1);
    final String[] temporaryArray = queryProcessed.split(" ");
    final String databaseName = temporaryArray[2];
    final String databasePath = DATABASE_PATH + databaseName;
    final File database = new File(databasePath);
    final boolean isDatabaseExists = database.isDirectory();
    if (isDatabaseExists) {
      final File[] files = new File(DATABASE_PATH).listFiles();
      if (files == null) {
        final String message = "Error: USE " + databaseName + " error!" + " | " +
            "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
        eventLogController.storeQueryLog(message, Instant.now());
        throw new QueryProcessorException(message);
      }
      for (final File file : files) {
        if (file.getName().equalsIgnoreCase(databaseName)) {
          this.useDatabaseName = file.getName();
        }
      }
      final String message = "Database " + this.useDatabaseName + " in use!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      generalLogController.storeQueryLog(message, Instant.now());
      return new QueryProcessorResponse(true, message);
    } else {
      final String message = "Error: Database " + databaseName + " does not exist!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
  }

  private QueryProcessorResponse createTable(final String query,
                                             final String tableName,
                                             final String tablePath)
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    Lock.applyExclusiveLock(this.useDatabaseName, tableName); // Apply Exclusive Lock
    final Pattern pattern = Pattern.compile(CREATE_TABLE_COLUMN_REGEX);
    final Matcher matcher = pattern.matcher(query);
    if (matcher.find()) {
      final String queryProcessed = matcher.group();
      final String[] columnTokens = queryProcessed.split(",");
      try (final FileWriter fileWriter = new FileWriter(tablePath)) {
        final StringBuilder createStringBuilder = new StringBuilder();
        for (final String columnToken : columnTokens) {
          final String[] tokens = columnToken.trim().split(" ");
          if (tokens.length == 2) {
            createStringBuilder.append(tokens[0])
                .append("(").append(tokens[1])
                .append(")")
                .append(DELIMITER_APPEND);
          }
          if (tokens.length == 4 && tokens[2].equalsIgnoreCase("PRIMARY")) {
            createStringBuilder.append(tokens[0])
                .append("(").append(tokens[1]).append("|")
                .append("PK")
                .append(")")
                .append(DELIMITER_APPEND);
          }
          if (tokens.length == 4 && tokens[2].equalsIgnoreCase("REFERENCES")) {
            final String foreignKeyTable = tokens[3].split("\\(")[0];
            String foreignKeyCol = tokens[3].split("\\(")[1].replaceAll("\\)", "");
            createStringBuilder.append(tokens[0]).append("(")
                .append(tokens[1]).append("|")
                .append("FK").append("|")
                .append(foreignKeyTable).append("|")
                .append(foreignKeyCol)
                .append(")")
                .append(DELIMITER_APPEND);
          }
        }
        createStringBuilder.replace(createStringBuilder.length() - DELIMITER_APPEND.length(), createStringBuilder.length(), "");
        createStringBuilder.append("\n");
        fileWriter.append(createStringBuilder.toString());
        Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
        final String message = "Table " + tableName + " created successfully!" + " | " +
            "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
        generalLogController.storeQueryLog(message, Instant.now());
        return new QueryProcessorResponse(true, message);
      } catch (final IOException e) {
        Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
        final String message = "Error: " + e.getMessage() + " | " +
            "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
        eventLogController.storeQueryLog(message, Instant.now());
        throw new QueryProcessorException(message);
      }
    } else {
      final String message = "Error: Something went wrong. Please try again!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
      throw new QueryProcessorException(message);
    }
  }

  private QueryProcessorResponse processCreateTableQuery(final String query)
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    if (!isDatabaseSelected()) {
      final String message = "Error: No database set!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String queryProcessed = query.substring(0, query.length() - 1);
    final String[] temporaryArray = queryProcessed.split(" ");
    final String tableName = temporaryArray[2];
    final String databasePath = DATABASE_PATH + this.useDatabaseName;
    final File database = new File(databasePath);
    final boolean isDatabaseExists = database.isDirectory();
    if (!isDatabaseExists) {
      final String message = "Error: Database " + this.useDatabaseName + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String tablePath = DATABASE_PATH + this.useDatabaseName + "/";
    final File allTablesPath = new File(tablePath);
    final File[] allTables = allTablesPath.listFiles();
    if (allTables == null) {
      final String message = "Error: Something went wrong. Please try again!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    boolean isTableExists = false;
    for (final File table : allTables) {
      if (table.getName().equalsIgnoreCase(tableName + ".txt")) {
        isTableExists = true;
      }
    }
    if (isTableExists) {
      final String message = "Error: Table " + tableName + " already exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final QueryProcessorResponse createdTableResponse =
        createTable(queryProcessed, tableName, tablePath + tableName + ".txt");
    try {
      this.metaDataGeneratorController.generateMetaData(this.useDatabaseName, tableName);
      return createdTableResponse;
    } catch (final MetaDataException e) {
      final String message = "Error: {" + e.getMessage() + "}!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
  }

  private QueryProcessorResponse processSelectAllQuery(final String query)
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    if (!isDatabaseSelected()) {
      final String message = "Error: No database set!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String queryProcessed = query.substring(0, query.length() - 1);
    final String[] temporaryArray = queryProcessed.split(" ");
    final String tableName = temporaryArray[3];
    final String databasePath = DATABASE_PATH + this.useDatabaseName;
    final File database = new File(databasePath);
    final boolean isDatabaseExists = database.isDirectory();
    if (!isDatabaseExists) {
      final String message = "Error: Database " + this.useDatabaseName + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String tablePath = DATABASE_PATH + this.useDatabaseName + "/";
    final File allTablesPath = new File(tablePath);
    final File[] allTables = allTablesPath.listFiles();
    if (allTables == null) {
      final String message = "Error: Something went wrong. Please try again!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    boolean isTableExists = false;
    for (final File table : allTables) {
      if (table.getName().equalsIgnoreCase(tableName + ".txt")) {
        isTableExists = true;
      }
    }
    if (!isTableExists) {
      final String message = "Error: Table " + tableName + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    Lock.applySharedLock(this.useDatabaseName, tableName); // Apply Shared Lock
    final String tableFullPath = tablePath + tableName + ".txt";
    try (final FileReader fileReader = new FileReader(tableFullPath);
         final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      final StringBuilder selectStringBuilder = new StringBuilder();
      String tuple;
      boolean isHeading = true;
      while ((tuple = bufferedReader.readLine()) != null) {
        final String[] rawColumns = tuple.split(DELIMITER);
        selectStringBuilder.append("| ");
        if (isHeading) {
          for (final String column : rawColumns) {
            selectStringBuilder.append(column.split("\\(")[0]).append(" | ");
          }
          selectStringBuilder.append("\n");
          isHeading = false;
        } else {
          for (final String column : rawColumns) {
            selectStringBuilder.append(column).append(" | ");
          }
          selectStringBuilder.append("\n");
        }
      }
      Lock.releaseSharedLock(this.useDatabaseName, tableName); // Release Shared Lock
      final String message = "Data from table " + this.useDatabaseName + "." + tableName + " read successfully!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      generalLogController.storeQueryLog(message, Instant.now());
      return new QueryProcessorResponse(true, selectStringBuilder.toString());
    } catch (final IOException e) {
      Lock.releaseSharedLock(this.useDatabaseName, tableName); // Release Shared Lock
      final String message = "Error: Something went wrong. Please try again!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
  }

  private QueryProcessorResponse processSelectDistinctQuery(final String query)
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    if (!isDatabaseSelected()) {
      final String message = "Error: No database set!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String queryProcessed = query.substring(0, query.length() - 1);
    final String[] temporaryArray = queryProcessed.split(" ");
    final String tableName = temporaryArray[4];
    final String databasePath = DATABASE_PATH + this.useDatabaseName;
    final File database = new File(databasePath);
    final boolean isDatabaseExists = database.isDirectory();
    if (!isDatabaseExists) {
      final String message = "Error: Database " + this.useDatabaseName + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String tablePath = DATABASE_PATH + this.useDatabaseName + "/";
    final File allTablesPath = new File(tablePath);
    final File[] allTables = allTablesPath.listFiles();
    if (allTables == null) {
      final String message = "Error: Something went wrong. Please try again!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    boolean isTableExists = false;
    for (final File table : allTables) {
      if (table.getName().equalsIgnoreCase(tableName + ".txt")) {
        isTableExists = true;
      }
    }
    if (!isTableExists) {
      final String message = "Error: Table " + tableName + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    Lock.applySharedLock(this.useDatabaseName, tableName); // Apply Shared Lock
    final String columnName = temporaryArray[2];
    final String tableFullPath = tablePath + tableName + ".txt";
    try (final FileReader fileReader = new FileReader(tableFullPath);
         final BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      final StringBuilder selectStringBuilder = new StringBuilder();
      String tuple;
      boolean isHeading = true;
      int columnIndexInInterest = -1;
      final Set<String> uniqueElements = new LinkedHashSet<>();
      while ((tuple = bufferedReader.readLine()) != null) {
        final String[] rawColumns = tuple.split(DELIMITER);
        if (isHeading) {
          for (int i = 0; i < rawColumns.length; i++) {
            final String column = rawColumns[i];
            if (columnName.equals((column.split("\\(")[0]))) {
              columnIndexInInterest = i;
              selectStringBuilder.append("| ").append(column.split("\\(")[0]).append(" | ").append("\n");
              break;
            }
          }
          if (columnIndexInInterest == -1) {
            throw new QueryProcessorException("Column " + columnName + " does not exists!");
          }
          isHeading = false;
        } else {
          uniqueElements.add(rawColumns[columnIndexInInterest]);
        }
      }
      for (String element : uniqueElements) {
        selectStringBuilder.append("| ").append(element).append(" | ").append("\n");
      }
      Lock.releaseSharedLock(this.useDatabaseName, tableName); // Release Shared Lock
      final String message = "Data from table " + this.useDatabaseName + "." + tableName + " read successfully!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      generalLogController.storeQueryLog(message, Instant.now());
      return new QueryProcessorResponse(true, selectStringBuilder.toString());
    } catch (final IOException e) {
      Lock.releaseSharedLock(this.useDatabaseName, tableName); // Release Shared Lock
      final String message = "Error: Something went wrong. Please try again!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
  }

  private QueryProcessorResponse insertData(final String query,
                                            final String tableName,
                                            final String tablePath)
      throws QueryProcessorException {
    Lock.applyExclusiveLock(this.useDatabaseName, tableName); // Apply Exclusive Lock
    final Pattern pattern1 = Pattern.compile(INSERT_COLUMN_NAME_REGEX);
    final Matcher matcher1 = pattern1.matcher(query);
    if (matcher1.find()) {
      final String allColumns = matcher1.group();
      final String[] columnTokensArray = allColumns.replace(")", "").split(",");
      final Set<String> columnTokens = new HashSet<>(Arrays.asList(columnTokensArray));
      final int numberOfColumnTokens = columnTokens.size();
      final Pattern pattern2 = Pattern.compile(INSERT_COLUMN_VALUE_REGEX);
      final Matcher matcher2 = pattern2.matcher(query);
      if (matcher2.find()) {
        final String allColumnValues = matcher2.group().substring(8, matcher2.group().length() - 1);
        final String[] columnValues = allColumnValues.replace("\"", "").split(",");
        if (numberOfColumnTokens != columnValues.length) {
          Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
          throw new QueryProcessorException("Number of columns and values do not match!");
        }
        final Map<String, String> columnValueDetails = new LinkedHashMap<>();
        for (int i = 0; i < numberOfColumnTokens; i++) {
          columnValueDetails.put(columnTokensArray[i].trim(), columnValues[i].trim());
        }
        try (final FileWriter fileWriter = new FileWriter(tablePath, true);
             final BufferedReader bufferedReader = new BufferedReader(new FileReader(tablePath))) {
          final String columnDefinition = bufferedReader.readLine();
          if (columnDefinition == null || columnDefinition.isEmpty()) {
            Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
            throw new QueryProcessorException("Fields in the table are not defined!");
          }
          final String[] columnDefinitionTokens = columnDefinition.split(DELIMITER);
          if (columnDefinitionTokens.length != numberOfColumnTokens) {
            Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
            throw new QueryProcessorException("Number of fields in the table do not match!");
          }
          final LinkedHashMap<String, String> columnDetails = new LinkedHashMap<>();
          for (final String columnDefinitionToken : columnDefinitionTokens) {
            final String[] temporaryTokens = columnDefinitionToken.replace(")", "").split("\\(");
            columnDetails.put(temporaryTokens[0].replace("(", ""), temporaryTokens[1].split("\\|")[0]);
          }
          for (int i = 0; i < columnDefinitionTokens.length; i++) {
            String[] temporaryTokens = columnDefinitionTokens[i].replace(")", "").split("\\(");
            if (!columnTokensArray[i].trim().equalsIgnoreCase(temporaryTokens[0].replace("(", ""))) {
              Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
              throw new QueryProcessorException("Sequence of columns does not match!");
            }
          }
          final StringBuilder insertStringBuilder = new StringBuilder();
          for (final String columnName : columnValueDetails.keySet()) {
            final String columnDataType = columnDetails.get(columnName);
            final String columnValue = columnValueDetails.get(columnName);
            if (columnDataType == null || columnDataType.isEmpty()) {
              Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
              throw new QueryProcessorException("Invalid data type set for column!");
            }
            if (columnDataType.equalsIgnoreCase(INT_DATATYPE)) {
              try {
                Integer.parseInt(columnValue);
              } catch (final NumberFormatException e) {
                Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
                throw new QueryProcessorException(e.getMessage());
              }
            }
            if (columnDataType.equalsIgnoreCase(FLOAT_DATATYPE)) {
              try {
                Float.parseFloat(columnValue);
              } catch (final NumberFormatException e) {
                Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
                throw new QueryProcessorException(e.getMessage());
              }
            }
            if (columnDataType.equalsIgnoreCase(BOOLEAN_DATATYPE)) {
              boolean value = Boolean.parseBoolean(columnValue);
              if (!value) {
                Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
                throw new QueryProcessorException("Invalid data type set for column!");
              }
            }
            insertStringBuilder.append(columnValue).append(DELIMITER_APPEND);
          }
          insertStringBuilder.replace(insertStringBuilder.length() - DELIMITER_APPEND.length(), insertStringBuilder.length(), "");
          insertStringBuilder.append("\n");
          fileWriter.append(insertStringBuilder.toString());
          Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
          return new QueryProcessorResponse(true, "Data inserted in table " + tableName + " successfully!");
        } catch (final IOException e) {
          Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
          throw new QueryProcessorException(e.getMessage());
        }
      } else {
        Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
        throw new QueryProcessorException("Invalid INSERT query!");
      }
    } else {
      Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
      throw new QueryProcessorException("Invalid INSERT query!");
    }
  }

  private QueryProcessorResponse processInsertDataQuery(final String query)
      throws QueryProcessorException {
    if (!isDatabaseSelected()) {
      throw new QueryProcessorException("Database not set!");
    }
    final String queryProcessed = query.substring(0, query.length() - 1);
    final String[] temporaryArray = queryProcessed.split(" ");
    final String tableName = temporaryArray[2];
    final String databasePath = DATABASE_PATH + useDatabaseName;
    final File database = new File(databasePath);
    final boolean isDatabaseExists = database.isDirectory();
    if (!isDatabaseExists) {
      throw new QueryProcessorException("Database does not exists!");
    }
    final String tablePath = DATABASE_PATH + useDatabaseName + "/";
    final File allTablesPath = new File(tablePath);
    final File[] allTables = allTablesPath.listFiles();
    if (allTables == null) {
      throw new QueryProcessorException("Something went wrong. Please try again!");
    }
    boolean isTableExists = false;
    for (File table : allTables) {
      if (table.getName().equalsIgnoreCase(tableName + ".txt")) {
        isTableExists = true;
      }
    }
    if (!isTableExists) {
      throw new QueryProcessorException("Table does not exists!");
    }
    return insertData(queryProcessed, tableName, tablePath + tableName + ".txt");
  }

  private QueryProcessorResponse processDropTableQuery(final String query)
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    if (!isDatabaseSelected()) {
      final String message = "Error: No database set!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String queryProcessed = query.substring(0, query.length() - 1);
    final String[] temporaryArray = queryProcessed.split(" ");
    final String tableName = temporaryArray[2];
    final String databasePath = DATABASE_PATH + this.useDatabaseName;
    final File database = new File(databasePath);
    final boolean isDatabaseExists = database.isDirectory();
    if (!isDatabaseExists) {
      final String message = "Error: Database " + this.useDatabaseName + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final File[] allTables = database.listFiles();
    if (allTables == null) {
      final String message = "Error: Something went wrong. Please try again!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    boolean isTableExists = false;
    for (final File table : allTables) {
      if (table.getName().equalsIgnoreCase(tableName + ".txt")) {
        Lock.applyExclusiveLock(this.useDatabaseName, tableName); // Apply Exclusive Lock
        final boolean isTableDeleted = table.delete();
        Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
        if (!isTableDeleted) {
          final String message = "Error: Unable to drop table " + tableName + " !" + " | " +
              "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
          eventLogController.storeQueryLog(message, Instant.now());
          throw new QueryProcessorException(message);
        }
        isTableExists = true;
      }
    }
    if (!isTableExists) {
      final String message = "Error: Table " + tableName + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String message = "Table " + tableName + " dropped successfully!" + " | " +
        "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
    generalLogController.storeQueryLog(message, Instant.now());
    return new QueryProcessorResponse(true, message);
  }

  private QueryProcessorResponse processTruncateTableQuery(final String query)
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    if (!isDatabaseSelected()) {
      final String message = "Error: No database set!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String queryProcessed = query.substring(0, query.length() - 1);
    final String[] temporaryArray = queryProcessed.split(" ");
    final String tableName = temporaryArray[2];
    final String databasePath = DATABASE_PATH + useDatabaseName;
    final File database = new File(databasePath);
    final boolean isDatabaseExists = database.isDirectory();
    if (!isDatabaseExists) {
      final String message = "Error: Database " + this.useDatabaseName + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final File[] allTables = database.listFiles();
    if (allTables == null) {
      final String message = "Error: Something went wrong. Please try again!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    boolean isTableExists = false;
    for (final File table : allTables) {
      final String name = table.getName();
      if (name.equalsIgnoreCase(tableName + ".txt")) {
        isTableExists = true;
        break;
      }
    }
    if (!isTableExists) {
      throw new QueryProcessorException("Table does not exists!");
    }
    Lock.applyExclusiveLock(this.useDatabaseName, tableName); // Apply Exclusive Lock
    final String tablePath = DATABASE_PATH + useDatabaseName + "/" + tableName + ".txt";
    try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(tablePath))) {
      final String writeToFile = bufferedReader.readLine();
      if (writeToFile == null || writeToFile.isEmpty()) {
        Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
        throw new QueryProcessorException("No table definition exists");
      }
      final FileWriter fileWriter = new FileWriter(tablePath, false);
      fileWriter.write(writeToFile + "\n");
      fileWriter.close();
    } catch (final IOException e) {
      Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
      throw new QueryProcessorException(e.getMessage());
    }
    Lock.releaseExclusiveLock(this.useDatabaseName, tableName); // Release Exclusive Lock
    return new QueryProcessorResponse(true, "Table " + tableName + " truncated successfully!");
  }

  private QueryProcessorResponse processStartTransactionQuery()
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    if (!isDatabaseSelected()) {
      final String message = "Error: No database set!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String inServerDBPath = DATABASE_SERVER_PATH + "/" + this.useDatabaseName + "/";
    final String inMemoryDBPath = DATABASE_IN_MEMORY_PATH + "/" + this.useDatabaseName + "/";
    final File inMemoryDB = new File(inMemoryDBPath);
    if (inMemoryDB.mkdir()) {
      final File inServerDBPathFolder = new File(inServerDBPath);
      final File[] inServerDBPathTables = inServerDBPathFolder.listFiles();
      if (inServerDBPathTables == null) {
        final String message = "Error: Something went wrong. Transaction execution failed!" + " | " +
            "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
        eventLogController.storeQueryLog(message, Instant.now());
        throw new QueryProcessorException(message);
      }
      for (final File table : inServerDBPathTables) {
        final Path src = Paths.get(inServerDBPath + table.getName());
        final Path dest = Paths.get(inMemoryDBPath + table.getName());
        try {
          Files.copy(src, dest);
        } catch (final IOException e) {
          e.printStackTrace();
          final String message = "Error: {" + e.getMessage() + "}!" + " | " +
              "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
          eventLogController.storeQueryLog(message, Instant.now());
          throw new QueryProcessorException(message);
        }
      }
      final String message = "Transaction started for database " + this.useDatabaseName + "!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      generalLogController.storeQueryLog(message, Instant.now());
      updateDatabasePath(true);
      return new QueryProcessorResponse(true, message);
    } else {
      final String message = "Error: Something went wrong. Transaction execution failed!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
  }

  private QueryProcessorResponse processCommitQuery()
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    if (!isTransactionFlow) {
      final String message = "Error: Transaction not started" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String inMemoryDatabasePath = DATABASE_PATH + "/" + this.useDatabaseName + "/";
    final File inMemoryDatabase = new File(inMemoryDatabasePath);
    final boolean isInMemoryDatabaseExists = inMemoryDatabase.isDirectory();
    if (!isInMemoryDatabaseExists) {
      final String message = "Error: Database " + inMemoryDatabase + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      updateDatabasePath(false);
      throw new QueryProcessorException(message);
    }
    final String inServerDatabasePath = DATABASE_SERVER_PATH + "/" + this.useDatabaseName + "/";
    final File inServerDatabase = new File(inServerDatabasePath);
    final boolean isInServerDatabaseExists = inServerDatabase.isDirectory();
    if (!isInServerDatabaseExists) {
      final String message = "Error: Database " + inServerDatabase + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      updateDatabasePath(false);
      throw new QueryProcessorException(message);
    }
    final File[] allTables = inServerDatabase.listFiles();
    if (allTables == null) {
      final String message = "Error: Database " + inServerDatabase + " failed to delete!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      updateDatabasePath(false);
      throw new QueryProcessorException(message);
    }
    for (final File table : allTables) {
      final boolean isTableDeleted = table.delete();
      if (!isTableDeleted) {
        final String message = "Error: Failed to delete tables of the database " + inServerDatabase + " Please try again!" + " | " +
            "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
        eventLogController.storeQueryLog(message, Instant.now());
        updateDatabasePath(false);
        throw new QueryProcessorException(message);
      }
    }
    final boolean isInServerDatabaseDeleted = inServerDatabase.delete();
    if (!isInServerDatabaseDeleted) {
      final String message = "Error: Database " + inServerDatabase + " deletion error!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      updateDatabasePath(false);
      throw new QueryProcessorException(message);
    }
    if (inServerDatabase.mkdir()) {
      final File[] inMemoryDatabaseTables = inMemoryDatabase.listFiles();
      if (inMemoryDatabaseTables == null) {
        final String message = "Error: Something went wrong. Transaction execution failed!" + " | " +
            "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
        eventLogController.storeQueryLog(message, Instant.now());
        updateDatabasePath(false);
        throw new QueryProcessorException(message);
      }
      for (final File table : inMemoryDatabaseTables) {
        final Path src = Paths.get(inMemoryDatabasePath + table.getName());
        final Path dest = Paths.get(inServerDatabasePath + table.getName());
        try {
          Files.copy(src, dest);
        } catch (final IOException e) {
          e.printStackTrace();
          final String message = "Error: {" + e.getMessage() + "}!" + " | " +
              "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
          eventLogController.storeQueryLog(message, Instant.now());
          updateDatabasePath(false);
          throw new QueryProcessorException(message);
        }
      }
      for (final File table : inMemoryDatabaseTables) {
        final boolean isTableDeleted = table.delete();
        if (!isTableDeleted) {
          final String message = "Error: Failed to delete tables of the database " + inMemoryDatabase + " Please try again!" + " | " +
              "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
          eventLogController.storeQueryLog(message, Instant.now());
          updateDatabasePath(false);
          throw new QueryProcessorException(message);
        }
      }
      final boolean isInMemoryDatabaseDeleted = inMemoryDatabase.delete();
      if (!isInMemoryDatabaseDeleted) {
        final String message = "Error: Database " + inMemoryDatabase + " deletion error!" + " | " +
            "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
        eventLogController.storeQueryLog(message, Instant.now());
        updateDatabasePath(false);
        throw new QueryProcessorException(message);
      }
      final String message = "Transaction committed!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      generalLogController.storeQueryLog(message, Instant.now());
      updateDatabasePath(false);
      return new QueryProcessorResponse(true, message);
    } else {
      final String message = "Error: Something went wrong. Transaction execution failed!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      updateDatabasePath(false);
      throw new QueryProcessorException(message);
    }
  }

  private QueryProcessorResponse processRollbackQuery()
      throws QueryProcessorException {
    final Instant startTime = Instant.now();
    if (!isTransactionFlow) {
      final String message = "Error: Transaction not started" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new QueryProcessorException(message);
    }
    final String inMemoryDatabasePath = DATABASE_PATH + this.useDatabaseName;
    final File inMemoryDatabase = new File(inMemoryDatabasePath);
    final boolean isDatabaseExists = inMemoryDatabase.isDirectory();
    if (!isDatabaseExists) {
      final String message = "Error: Database " + inMemoryDatabase + " does not exists!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      updateDatabasePath(false);
      throw new QueryProcessorException(message);
    }
    final File[] allTables = inMemoryDatabase.listFiles();
    if (allTables == null) {
      final String message = "Error: Database " + inMemoryDatabase + " failed to delete!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      updateDatabasePath(false);
      throw new QueryProcessorException(message);
    }
    for (final File table : allTables) {
      final boolean isTableDeleted = table.delete();
      if (!isTableDeleted) {
        final String message = "Error: Failed to delete tables of the database " + inMemoryDatabase + " Please try again!" + " | " +
            "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
        eventLogController.storeQueryLog(message, Instant.now());
        updateDatabasePath(false);
        throw new QueryProcessorException(message);
      }
    }
    final boolean isDatabaseDeleted = inMemoryDatabase.delete();
    if (isDatabaseDeleted) {
      final String message = "Transaction rollback!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      generalLogController.storeQueryLog(message, Instant.now());
      updateDatabasePath(false);
      return new QueryProcessorResponse(true, message);
    } else {
      final String message = "Error: Database " + inMemoryDatabase + " deletion error!" + " | " +
          "Execution Time: " + getQueryExecutionTime(startTime, Instant.now()) + "ms";
      eventLogController.storeQueryLog(message, Instant.now());
      updateDatabasePath(false);
      throw new QueryProcessorException(message);
    }
  }

  public QueryProcessorResponse processQuery(final String query)
      throws QueryParserException, QueryProcessorException {
    queryLogController.storeQueryLog(query, Instant.now());
    final boolean isQueryValid = queryParserController.parseQuery(query);
    if (!isQueryValid) {
      eventLogController.storeQueryLog("Error: Invalid query " + query, Instant.now());
      throw new QueryParserException("Invalid query!");
    }
    final String queryLC = query.trim().toLowerCase();
    if (queryLC.contains(CREATE_DATABASE_KEYWORD)) {
      return processCreateDatabaseQuery(query);
    } else if (queryLC.contains(DROP_DATABASE_KEYWORD)) {
      return processDropDatabaseQuery(query);
    } else if (queryLC.contains(USE_DATABASE_KEYWORD)) {
      return processUseDatabaseQuery(query);
    } else if (queryLC.contains(CREATE_TABLE_KEYWORD)) {
      return processCreateTableQuery(query);
    } else if (queryLC.contains(SELECT_KEYWORD)) {
      if (queryLC.contains(SELECT_ALL_KEYWORD)) {
        return processSelectAllQuery(query);
      }
      if (queryLC.contains(SELECT_DISTINCT_KEYWORD)) {
        return processSelectDistinctQuery(query);
      }
    } else if (queryLC.contains(INSERT_KEYWORD)) {
      return processInsertDataQuery(query);
    } else if (queryLC.contains(DROP_TABLE_KEYWORD)) {
      return processDropTableQuery(query);
    } else if (queryLC.contains(TRUNCATE_TABLE_KEYWORD)) {
      return processTruncateTableQuery(query);
    } else if (queryLC.contains(START_TRANSACTION_KEYWORD)) {
      return processStartTransactionQuery();
    } else if (queryLC.contains(COMMIT_KEYWORD)) {
      return processCommitQuery();
    } else if (queryLC.contains(ROLLBACK_KEYWORD)) {
      return processRollbackQuery();
    } else {
      throw new QueryProcessorException("Error processing the query!");
    }
    throw new QueryProcessorException("Error processing the query!");
  }
}