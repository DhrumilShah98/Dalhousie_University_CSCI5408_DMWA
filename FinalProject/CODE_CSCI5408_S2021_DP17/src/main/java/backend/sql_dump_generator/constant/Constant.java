package backend.sql_dump_generator.constant;

public final class Constant {

  public static final String DELIMITER =
      "\\$\\$\\@\\@\\|\\|\\|\\|\\@\\@\\$\\$";
  public static final String DATABASE_PATH =
      "./src/main/java/database/database_server/";
  public static final String SQL_DUMPS_PATH =
      "./src/main/java/output/sql_dumps/";
  public static final String TEXT_DATATYPE =
      "TEXT";
  public static final String INT_DATATYPE =
      "INT";
  public static final String FLOAT_DATATYPE =
      "FLOAT";
  public static final String BOOLEAN_DATATYPE =
      "BOOLEAN";
  public static final String PRIMARY_KEY_CONSTRAINT =
      "PK";
  public static final String FOREIGN_KEY_CONSTRAINT =
      "FK";
}