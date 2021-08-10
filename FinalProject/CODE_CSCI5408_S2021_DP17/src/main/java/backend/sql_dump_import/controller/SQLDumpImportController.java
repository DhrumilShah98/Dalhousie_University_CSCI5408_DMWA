package backend.sql_dump_import.controller;

import backend.log_generator.controller.EventLogController;
import backend.log_generator.controller.GeneralLogController;
import backend.query_parser.exception.QueryParserException;
import backend.query_processor.controller.QueryProcessorController;
import backend.query_processor.exception.QueryProcessorException;
import backend.sql_dump_import.exception.SQLDumpImportException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;

import static backend.sql_dump_import.constant.Constant.*;

public final class SQLDumpImportController {

  private final GeneralLogController generalLogController;
  private final EventLogController eventLogController;
  private final QueryProcessorController queryProcessorController;

  public SQLDumpImportController() {
    this.queryProcessorController = new QueryProcessorController();
    this.generalLogController = new GeneralLogController();
    this.eventLogController = new EventLogController();
  }

  private void importDump(final String sqlFileName,
                          final String sqlFilePath)
      throws SQLDumpImportException {
    final String databaseName = sqlFileName.split("-")[0] + System.currentTimeMillis();
    try (final FileReader sqlFileReader = new FileReader(sqlFilePath);
         final BufferedReader sqlFileBufferedReader = new BufferedReader(sqlFileReader)) {

      queryProcessorController.processQuery("CREATE DATABASE " + databaseName + ";");
      queryProcessorController.processQuery("USE DATABASE " + databaseName + ";");
      String currentLine;
      while ((currentLine = sqlFileBufferedReader.readLine()) != null) {
        queryProcessorController.processQuery(currentLine);
      }
    } catch (IOException | QueryParserException | QueryProcessorException e) {
      final String message = "Error: {" + e.getMessage() + "}!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new SQLDumpImportException(message);
    }
  }

  public boolean importSQLDump(final String sqlFileName)
      throws SQLDumpImportException {
    if (sqlFileName == null || sqlFileName.isEmpty() || !sqlFileName.endsWith(".sql")) {
      final String message = "Error: Failed to import SQL DUMP for file " + sqlFileName + ". Invalid file!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new SQLDumpImportException(message);
    }

    final String sqlFilePath = SQL_DUMPS_PATH + sqlFileName;
    if (!Files.exists(Paths.get(sqlFilePath))) {
      final String message = "Error: Invalid file path " + sqlFilePath + " or file does not exists!";
      eventLogController.storeQueryLog(message, Instant.now());
      throw new SQLDumpImportException(message);
    }
    importDump(sqlFileName, sqlFilePath);
    generalLogController.storeQueryLog("SQL Dump imported successfully for file " + sqlFileName + "!",
        Instant.now());
    return true;
  }
}
