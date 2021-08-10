package backend.query_processor.constant;

public final class Constant {

  public static final String DELIMITER =
      "\\$\\$\\@\\@\\|\\|\\|\\|\\@\\@\\$\\$";
  public static final String DELIMITER_APPEND =
      "$$@@||||@@$$";
  public static final String CREATE_TABLE_COLUMN_REGEX =
      "([a-zA-Z\\d]+\\s(INT|TEXT|FLOAT|BOOLEAN)(\\sPRIMARY KEY|\\sREFERENCES\\s[a-zA-Z\\d]+\\([a-zA-Z\\d]+\\))?(,\\s[a-zA-Z\\d]+\\s(INT|TEXT|FLOAT|BOOLEAN)(\\sPRIMARY KEY|\\sREFERENCES\\s[a-zA-Z\\d]+\\([a-zA-Z\\d]+\\))?)*)";
  public static final String INSERT_COLUMN_NAME_REGEX =
      "([a-zA-Z\\d]+(,\\s[a-zA-Z\\d]+)*\\))";
  public static final String INSERT_COLUMN_VALUE_REGEX =
      "VALUES\\s\\(\\\"?[a-zA-Z\\d\\s~`!@#$^&*-_+=|':;.,?]+\\\"?(,\\s\\\"?[a-zA-Z\\d\\s~`!@#$^&*-_+=|':;.,?]+\\\"?)*\\)";
  public static final String TEXT_DATATYPE =
      "TEXT";
  public static final String INT_DATATYPE =
      "INT";
  public static final String FLOAT_DATATYPE =
      "FLOAT";
  public static final String BOOLEAN_DATATYPE =
      "BOOLEAN";
  public static final String DATABASE_SERVER_PATH =
      "./src/main/java/database/database_server/";
  public static final String DATABASE_IN_MEMORY_PATH =
      "./src/main/java/database/database_in_memory/";
  public static boolean isTransactionFlow =
      false;
  public static String DATABASE_PATH =
      DATABASE_SERVER_PATH;

  public static void updateDatabasePath(boolean isTransactionFlow) {
    Constant.isTransactionFlow =
        isTransactionFlow;
    DATABASE_PATH =
        (isTransactionFlow) ? DATABASE_IN_MEMORY_PATH : DATABASE_SERVER_PATH;
  }
}