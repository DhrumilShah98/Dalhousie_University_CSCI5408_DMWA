package backend.query_parser.controller;

import backend.query_parser.exception.QueryParserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("QueryParserControllerTest class to test QueryParserController")
public final class QueryParserControllerTest {

  @Test
  @DisplayName("Test incorrect CREATE DATABASE queries")
  public void testIncorrectCreateDatabaseQuery() {
    final String invalidCreateDBQuery1 = "CREATE DATABASE stud@nts;";
    final String invalidCreateDBQuery2 = "CREATE DATABASE students@2021;";
    final String invalidCreateDBQuery3 = "CREATE DATABASE Students@2021;";
    final String invalidCreateDBQuery4 = "CRATE DATABASE students2021;";
    final String invalidCreateDBQuery5 = "CREATE DATBASE Students2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateDBQuery1));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateDBQuery2));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateDBQuery3));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateDBQuery4));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateDBQuery5));
  }

  @Test
  @DisplayName("Test correct CREATE DATABASE queries")
  public void testCorrectCreateDatabaseQuery() throws QueryParserException {
    final String validCreateDBQuery1 = "CREATE DATABASE students;";
    final String validCreateDBQuery2 = "CREATE DATABASE students2021;";
    final String validCreateDBQuery3 = "CREATE DATABASE Students2021;";
    final String validCreateDBQuery4 = "create DATABASE students;";
    final String validCreateDBQuery5 = "CREATE database students2021;";
    final String validCreateDBQuery6 = "create database Students2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validCreateDBQuery1));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateDBQuery2));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateDBQuery3));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateDBQuery4));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateDBQuery5));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateDBQuery6));
  }

  @Test
  @DisplayName("Test incorrect USE DATABASE queries")
  public void testIncorrectUseDatabaseQuery() {
    final String invalidUseDBQuery1 = "USE DATABASE stud@nts;";
    final String invalidUseDBQuery2 = "USE DATABASE students@2021;";
    final String invalidUseDBQuery3 = "USE DATABASE Students@2021;";
    final String invalidUseDBQuery4 = "UE DATABASE students2021;";
    final String invalidUseDBQuery5 = "USE DATBASE Students2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidUseDBQuery1));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidUseDBQuery2));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidUseDBQuery3));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidUseDBQuery4));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidUseDBQuery5));
  }

  @Test
  @DisplayName("Test correct USE DATABASE queries")
  public void testCorrectUseDatabaseQuery() throws QueryParserException {
    final String validUseDBQuery1 = "USE DATABASE students;";
    final String validUseDBQuery2 = "USE DATABASE students2021;";
    final String validUseDBQuery3 = "USE DATABASE Students2021;";
    final String validUseDBQuery4 = "use DATABASE students;";
    final String validUseDBQuery5 = "USE database students2021;";
    final String validUseDBQuery6 = "Use Database Students2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validUseDBQuery1));
    Assertions.assertTrue(queryParserController.parseQuery(validUseDBQuery2));
    Assertions.assertTrue(queryParserController.parseQuery(validUseDBQuery3));
    Assertions.assertTrue(queryParserController.parseQuery(validUseDBQuery4));
    Assertions.assertTrue(queryParserController.parseQuery(validUseDBQuery5));
    Assertions.assertTrue(queryParserController.parseQuery(validUseDBQuery6));
  }

  @Test
  @DisplayName("Test incorrect CREATE TABLE queries")
  public void testIncorrectCreateTableQuery() {
    final String invalidCreateTableQuery1 = "CREATE TABLE students (id INT;";
    final String invalidCreateTableQuery2 = "CREATE TABLE students (id IT);";
    final String invalidCreateTableQuery3 = "CREATE TABLE stud@nts (id INT);";
    final String invalidCreateTableQuery4 = "CREATE TABLE students (id INT name TEXT);";
    final String invalidCreateTableQuery5 = "CREATE TABLE students (id INT, name TXT);";
    final String invalidCreateTableQuery6 = "CREATE TABLE students (id INT, name DATE);";
    final String invalidCreateTableQuery7 = "CREATE TABLE students (id INT, name TEXT, emailTEXT);";
    final String invalidCreateTableQuery8 = "CRETE TABLE students (id INT, name DATE);";
    final String invalidCreateTableQuery9 = "CREATE TBLE students (id INT, name TEXT, emailTEXT);";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateTableQuery1));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateTableQuery2));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateTableQuery3));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateTableQuery4));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateTableQuery5));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateTableQuery6));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateTableQuery7));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateTableQuery8));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCreateTableQuery9));
  }

  @Test
  @DisplayName("Test correct CREATE TABLE queries")
  public void testCorrectCreateTableQuery() throws QueryParserException {
    final String validCreateTableQuery1 = "CREATE TABLE students (id INT);";
    final String validCreateTableQuery2 = "CREATE TABLE students (id INT, name TEXT);";
    final String validCreateTableQuery3 = "CREATE TABLE students (id INT, name TEXT, email TEXT);";
    final String validCreateTableQuery4 = "create TABLE students (id INT);";
    final String validCreateTableQuery5 = "CREATE table students (id INT, name TEXT);";
    final String validCreateTableQuery6 = "create table students (id INT, name TEXT, email TEXT);";
    final String validCreateTableQuery7 = "CREATE TABLE students (id INT PRIMARY KEY, name TEXT, email TEXT);";
    final String validCreateTableQuery8 = "CREATE TABLE employee (id INT PRIMARY KEY, departmentId INT REFERENCES department(id));";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validCreateTableQuery1));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateTableQuery2));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateTableQuery3));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateTableQuery4));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateTableQuery5));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateTableQuery6));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateTableQuery7));
    Assertions.assertTrue(queryParserController.parseQuery(validCreateTableQuery8));
  }

  @Test
  @DisplayName("Test incorrect DROP DATABASE queries")
  public void testIncorrectDropDatabaseQuery() {
    final String invalidDropDBQuery1 = "DROP DATABASE stud@nts;";
    final String invalidDropDBQuery2 = "DROP DATABASE students@2021;";
    final String invalidDropDBQuery3 = "DROP DATABASE Students@2021;";
    final String invalidDropDBQuery4 = "DRP DATABASE students@2021;";
    final String invalidDropDBQuery5 = "DROP DATABSE Students@2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidDropDBQuery1));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidDropDBQuery2));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidDropDBQuery3));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidDropDBQuery4));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidDropDBQuery5));
  }

  @Test
  @DisplayName("Test correct DROP DATABASE queries")
  public void testCorrectDropDatabaseQuery() throws QueryParserException {
    final String validDropDBQuery1 = "DROP DATABASE students;";
    final String validDropDBQuery2 = "DROP DATABASE students2021;";
    final String validDropDBQuery3 = "DROP DATABASE Students2021;";
    final String validDropDBQuery4 = "drop DATABASE students;";
    final String validDropDBQuery5 = "DROP database students2021;";
    final String validDropDBQuery6 = "drop database Students2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validDropDBQuery1));
    Assertions.assertTrue(queryParserController.parseQuery(validDropDBQuery2));
    Assertions.assertTrue(queryParserController.parseQuery(validDropDBQuery3));
    Assertions.assertTrue(queryParserController.parseQuery(validDropDBQuery4));
    Assertions.assertTrue(queryParserController.parseQuery(validDropDBQuery5));
    Assertions.assertTrue(queryParserController.parseQuery(validDropDBQuery6));
  }

  @Test
  @DisplayName("Test incorrect DROP TABLE queries")
  public void testIncorrectDropTableQuery() {
    final String invalidDropTableQuery1 = "DROP TABLE stud@nts;";
    final String invalidDropTableQuery2 = "DROP TABLE students@2021;";
    final String invalidDropTableQuery3 = "DROP TABLE Students@2021;";
    final String invalidDropTableQuery4 = "DRP TABLE students@2021;";
    final String invalidDropTableQuery5 = "DROP TBLE Students@2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidDropTableQuery1));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidDropTableQuery2));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidDropTableQuery3));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidDropTableQuery4));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidDropTableQuery5));
  }

  @Test
  @DisplayName("Test correct DROP TABLE queries")
  public void testCorrectDropTableQuery() throws QueryParserException {
    final String validDropTableQuery1 = "DROP TABLE students;";
    final String validDropTableQuery2 = "DROP TABLE students2021;";
    final String validDropTableQuery3 = "DROP TABLE Students2021;";
    final String validDropTableQuery4 = "drop TABLE students;";
    final String validDropTableQuery5 = "DROP table students2021;";
    final String validDropTableQuery6 = "drop table Students2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validDropTableQuery1));
    Assertions.assertTrue(queryParserController.parseQuery(validDropTableQuery2));
    Assertions.assertTrue(queryParserController.parseQuery(validDropTableQuery3));
    Assertions.assertTrue(queryParserController.parseQuery(validDropTableQuery4));
    Assertions.assertTrue(queryParserController.parseQuery(validDropTableQuery5));
    Assertions.assertTrue(queryParserController.parseQuery(validDropTableQuery6));
  }

  @Test
  @DisplayName("Test incorrect TRUNCATE TABLE queries")
  public void testIncorrectTruncateTableQuery() {
    final String invalidTruncateTableQuery1 = "TRUNCATE TABLE stud@nts;";
    final String invalidTruncateTableQuery2 = "TRUNCATE TABLE students@2021;";
    final String invalidTruncateTableQuery3 = "TRUNCATE TABLE Students@2021;";
    final String invalidTruncateTableQuery4 = "TRNCATE TABLE students@2021;";
    final String invalidTruncateTableQuery5 = "TRUNCATE TBLE Students@2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidTruncateTableQuery1));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidTruncateTableQuery2));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidTruncateTableQuery3));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidTruncateTableQuery4));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidTruncateTableQuery5));
  }

  @Test
  @DisplayName("Test correct TRUNCATE TABLE queries")
  public void testCorrectTruncateTableQuery() throws QueryParserException {
    final String validTruncateTableQuery1 = "TRUNCATE TABLE students;";
    final String validTruncateTableQuery2 = "TRUNCATE TABLE students2021;";
    final String validTruncateTableQuery3 = "TRUNCATE TABLE Students2021;";
    final String validTruncateTableQuery4 = "truncate TABLE students;";
    final String validTruncateTableQuery5 = "TRUNCATE table students2021;";
    final String validTruncateTableQuery6 = "truncate table Students2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validTruncateTableQuery1));
    Assertions.assertTrue(queryParserController.parseQuery(validTruncateTableQuery2));
    Assertions.assertTrue(queryParserController.parseQuery(validTruncateTableQuery3));
    Assertions.assertTrue(queryParserController.parseQuery(validTruncateTableQuery4));
    Assertions.assertTrue(queryParserController.parseQuery(validTruncateTableQuery5));
    Assertions.assertTrue(queryParserController.parseQuery(validTruncateTableQuery6));
  }

  @Test
  @DisplayName("Test incorrect SELECT ALL queries")
  public void testIncorrectSelectAllQuery() {
    final String invalidSelectAllQuery1 = "SELECT * FROM stud@nts;";
    final String invalidSelectAllQuery2 = "SELECT * FROM students@2021;";
    final String invalidSelectAllQuery3 = "SELECT * FROM Students@2021;";
    final String invalidSelectAllQuery4 = "SLECT * FROM students@2021;";
    final String invalidSelectAllQuery5 = "SELECT * FRM Students@2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectAllQuery1));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectAllQuery2));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectAllQuery3));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectAllQuery4));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectAllQuery5));
  }

  @Test
  @DisplayName("Test correct SELECT ALL queries")
  public void testCorrectSelectAllQuery() throws QueryParserException {
    final String validSelectAllQuery1 = "SELECT * FROM students;";
    final String validSelectAllQuery2 = "SELECT * FROM students2021;";
    final String validSelectAllQuery3 = "SELECT * FROM Students2021;";
    final String validSelectAllQuery4 = "select * FROM students;";
    final String validSelectAllQuery5 = "SELECT * from students2021;";
    final String validSelectAllQuery6 = "select * from Students2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validSelectAllQuery1));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectAllQuery2));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectAllQuery3));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectAllQuery4));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectAllQuery5));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectAllQuery6));
  }

  @Test
  @DisplayName("Test incorrect SELECT DISTINCT queries")
  public void testIncorrectSelectDistinctQuery() {
    final String invalidSelectDistinctQuery1 = "SELECT DISTINCT id, FROM stud@nts;";
    final String invalidSelectDistinctQuery2 = "SELECT DISTINCT n@me FROM students2021;";
    final String invalidSelectDistinctQuery3 = "SELECT DISTINCT name FROM Students@2021;";
    final String invalidSelectDistinctQuery4 = "SELCT DISTINCT id FROM students;";
    final String invalidSelectDistinctQuery5 = "SELECT DISTINT id FROM students;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectDistinctQuery1));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectDistinctQuery2));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectDistinctQuery3));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectDistinctQuery4));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectDistinctQuery5));
  }

  @Test
  @DisplayName("Test correct SELECT DISTINCT queries")
  public void testCorrectSelectDistinctQuery() throws QueryParserException {
    final String validSelectDistinctQuery1 = "SELECT DISTINCT id FROM students;";
    final String validSelectDistinctQuery2 = "select DISTINCT name FROM students2021;";
    final String validSelectDistinctQuery3 = "SELECT distinct name FROM Students2021;";
    final String validSelectDistinctQuery4 = "select distinct id FROM students;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validSelectDistinctQuery1));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectDistinctQuery2));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectDistinctQuery3));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectDistinctQuery4));
  }

  @Test
  @DisplayName("Test incorrect SELECT queries")
  public void testIncorrectSelectQuery() {
    final String invalidSelectQuery1 = "SELECT id, FROM stud@nts;";
    final String invalidSelectQuery2 = "SELECT id, n@me FROM students@2021;";
    final String invalidSelectQuery3 = "SELECT FROM Students@2021;";
    final String invalidSelectQuery4 = "SLECT FROM Students2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectQuery1));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectQuery2));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectQuery3));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidSelectQuery4));
  }

  @Test
  @DisplayName("Test correct SELECT queries")
  public void testCorrectSelectQuery() throws QueryParserException {
    final String validSelectQuery1 = "SELECT id FROM students;";
    final String validSelectQuery2 = "SELECT id, name FROM students2021;";
    final String validSelectQuery3 = "SELECT id, name, email FROM Students2021;";
    final String validSelectQuery4 = "SELECT id, name, email FROM Students2021;";
    final String validSelectQuery5 = "select id FROM students;";
    final String validSelectQuery6 = "SELECT id, name from students2021;";
    final String validSelectQuery7 = "select id, name, email from Students2021;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validSelectQuery1));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectQuery2));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectQuery3));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectQuery4));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectQuery5));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectQuery6));
    Assertions.assertTrue(queryParserController.parseQuery(validSelectQuery7));
  }

  @Test
  @DisplayName("Test incorrect INSERT queries")
  public void testIncorrectInsertQuery() {
    final String invalidInsertQuery1 = "INSERT INTO students (id, name) VALUES (1, \"Dhrumil\";";
    final String invalidInsertQuery2 = "INSERT INTO students (id name) VALUES (1, \"Dhrumil\");";
    final String invalidInsertQuery3 = "INSERT INTO students (id, name) VALUES (1 \"Dhrumil\");";
    final String invalidInsertQuery4 = "INSERT INTO students (id, name) (1, \"Dhrumil\");";
    final String invalidInsertQuery5 = "INSRT INTO students (id, name) VALUES (1, \"Dhrumil\");";
    final String invalidInsertQuery6 = "INSERT ITO students (id, name) VALUES (1, \"Dhrumil\");";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidInsertQuery1));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidInsertQuery2));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidInsertQuery3));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidInsertQuery4));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidInsertQuery5));
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidInsertQuery6));
  }

  @Test
  @DisplayName("Test correct INSERT queries")
  public void testCorrectInsertQuery() throws QueryParserException {
    final String validInsertQuery1 = "INSERT INTO students (id, name) VALUES (1, \"Dhrumil\");";
    final String validInsertQuery2 = "INSERT INTO students (id, name, email) VALUES (1, \"Dhrumil\", \"dhrumil@gmail.com\");";
    final String validInsertQuery3 = "INSERT INTO students (id, name, email) VALUES (1, \"Dhrumil Shah\", \"dhrumil@gmail.com\");";
    final String validInsertQuery4 = "INSERT INTO employee (id, departmentId, name, address, email) VALUES (1, 2, \"Dhrumil Shah\", \"2345 Avenue Halifax NS\", \"dhrumil@gmail.com\");";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validInsertQuery1));
    Assertions.assertTrue(queryParserController.parseQuery(validInsertQuery2));
    Assertions.assertTrue(queryParserController.parseQuery(validInsertQuery3));
    Assertions.assertTrue(queryParserController.parseQuery(validInsertQuery4));
  }

  @Test
  @DisplayName("Test incorrect START TRANSACTION queries")
  public void testIncorrectStartTransactionQuery() {
    final String invalidStartTransaction1 = "START TRANSACTION";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidStartTransaction1));
  }

  @Test
  @DisplayName("Test correct START TRANSACTION queries")
  public void testCorrectStartTransactionQuery() throws QueryParserException {
    final String validStartTransaction1 = "START TRANSACTION;";
    final String validStartTransaction2 = "start transaction;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validStartTransaction1));
    Assertions.assertTrue(queryParserController.parseQuery(validStartTransaction2));
  }

  @Test
  @DisplayName("Test incorrect COMMIT queries")
  public void testIncorrectCommitQuery() {
    final String invalidCommit1 = "COMMIT";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidCommit1));
  }

  @Test
  @DisplayName("Test correct COMMIT queries")
  public void testCorrectCommitQuery() throws QueryParserException {
    final String validCommit1 = "COMMIT;";
    final String validCommit2 = "commit;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validCommit1));
    Assertions.assertTrue(queryParserController.parseQuery(validCommit2));
  }

  @Test
  @DisplayName("Test incorrect ROLLBACK queries")
  public void testIncorrectRollbackQuery() {
    final String invalidRollback1 = "ROLLBACK";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertThrows(QueryParserException.class,
        () -> queryParserController.parseQuery(invalidRollback1));
  }

  @Test
  @DisplayName("Test correct ROLLBACK queries")
  public void testCorrectRollbackQuery() throws QueryParserException {
    final String validRollback1 = "ROLLBACK;";
    final String validRollback2 = "rollback;";
    final QueryParserController queryParserController = new QueryParserController();
    Assertions.assertTrue(queryParserController.parseQuery(validRollback1));
    Assertions.assertTrue(queryParserController.parseQuery(validRollback2));
  }
}