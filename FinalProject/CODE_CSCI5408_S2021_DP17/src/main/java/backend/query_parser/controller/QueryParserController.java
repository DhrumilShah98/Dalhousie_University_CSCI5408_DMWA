package backend.query_parser.controller;

import backend.query_parser.exception.QueryParserException;

import java.util.regex.Pattern;

import static backend.query_parser.constant.Constant.*;

public final class QueryParserController {

  public boolean parseQuery(final String query) throws QueryParserException {
    if (query == null || query.trim().isEmpty()) {
      throw new QueryParserException("Invalid query syntax!");
    }
    final String queryLC = query.trim().toLowerCase();
    if (queryLC.contains(CREATE_DATABASE_KEYWORD)) {
      final boolean isCreateDatabaseSyntaxCorrect = Pattern.matches(CREATE_DATABASE_QUERY_SYNTAX, queryLC);
      if (!isCreateDatabaseSyntaxCorrect) {
        throw new QueryParserException("Invalid CREATE DATABASE query!");
      }
    } else if (queryLC.contains(USE_DATABASE_KEYWORD)) {
      final boolean isUseDatabaseSyntaxCorrect = Pattern.matches(USE_DATABASE_QUERY_SYNTAX, queryLC);
      if (!isUseDatabaseSyntaxCorrect) {
        throw new QueryParserException("Invalid USE DATABASE query!");
      }
    } else if (queryLC.contains(CREATE_TABLE_KEYWORD)) {
      final boolean isCreateTableSyntaxCorrect = Pattern.matches(CREATE_TABLE_QUERY_SYNTAX, queryLC);
      if (!isCreateTableSyntaxCorrect) {
        throw new QueryParserException("Invalid CREATE TABLE query!");
      }
    } else if (queryLC.contains(DROP_DATABASE_KEYWORD)) {
      final boolean isDropDatabaseSyntaxCorrect = Pattern.matches(DROP_DATABASE_QUERY_SYNTAX, queryLC);
      if (!isDropDatabaseSyntaxCorrect) {
        throw new QueryParserException("Invalid DROP DATABASE query!");
      }
    } else if (queryLC.contains(DROP_TABLE_KEYWORD)) {
      final boolean isDropTableSyntaxCorrect = Pattern.matches(DROP_TABLE_QUERY_SYNTAX, queryLC);
      if (!isDropTableSyntaxCorrect) {
        throw new QueryParserException("Invalid DROP TABLE query!");
      }
    } else if (queryLC.contains(TRUNCATE_TABLE_KEYWORD)) {
      final boolean isTruncateTableSyntaxCorrect = Pattern.matches(TRUNCATE_TABLE_QUERY_SYNTAX, queryLC);
      if (!isTruncateTableSyntaxCorrect) {
        throw new QueryParserException("Invalid TRUNCATE TABLE query!");
      }
    } else if (queryLC.contains(SELECT_KEYWORD)) {
      if (queryLC.contains(SELECT_ALL_KEYWORD)) {
        final boolean isSelectAllSyntaxCorrect = Pattern.matches(SELECT_ALL_QUERY_SYNTAX, queryLC);
        if (!isSelectAllSyntaxCorrect) {
          throw new QueryParserException("Invalid SELECT ALL query!");
        }
      } else if (queryLC.contains(SELECT_DISTINCT_KEYWORD)) {
        final boolean isSelectDistinctSyntaxCorrect = Pattern.matches(SELECT_DISTINCT_QUERY_SYNTAX, queryLC);
        if (!isSelectDistinctSyntaxCorrect) {
          throw new QueryParserException("Invalid SELECT DISTINCT query!");
        }
      } else if (queryLC.contains(SELECT_SPECIFIC_KEYWORD)) {
        final boolean isSelectSyntaxCorrect = Pattern.matches(SELECT_SPECIFIC_QUERY_SYNTAX, queryLC);
        if (!isSelectSyntaxCorrect) {
          throw new QueryParserException("Invalid SELECT query!");
        }
      } else {
        throw new QueryParserException("Invalid SELECT query syntax!");
      }
    } else if (queryLC.contains(INSERT_KEYWORD)) {
      final boolean isInsertSyntaxCorrect = Pattern.matches(INSERT_QUERY_SYNTAX, queryLC);
      if (!isInsertSyntaxCorrect) {
        throw new QueryParserException("Invalid INSERT query!");
      }
    } else if (queryLC.contains(START_TRANSACTION_KEYWORD)) {
      final boolean isStartTransactionKeywordCorrect = Pattern.matches(START_TRANSACTION_QUERY, queryLC);
      if (!isStartTransactionKeywordCorrect) {
        throw new QueryParserException("Invalid START TRANSACTION query!");
      }
    } else if (queryLC.contains(COMMIT_KEYWORD)) {
      final boolean isCommitKeywordCorrect = Pattern.matches(COMMIT_QUERY, queryLC);
      if (!isCommitKeywordCorrect) {
        throw new QueryParserException("Invalid COMMIT query!");
      }
    } else if (queryLC.contains(ROLLBACK_KEYWORD)) {
      final boolean isStartTransactionKeywordCorrect = Pattern.matches(ROLLBACK_QUERY, queryLC);
      if (!isStartTransactionKeywordCorrect) {
        throw new QueryParserException("Invalid ROLLBACK query!");
      }
    } else {
      throw new QueryParserException("Invalid query syntax!");
    }
    return true;
  }
}