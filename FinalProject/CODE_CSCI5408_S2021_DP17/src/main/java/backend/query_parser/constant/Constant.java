package backend.query_parser.constant;

public final class Constant {

  public static final String CREATE_DATABASE_KEYWORD =
      "create database";
  public static final String CREATE_DATABASE_QUERY_SYNTAX =
      "^(?i)(CREATE\\sDATABASE\\s[a-zA-Z\\d]+;)$";
  public static final String USE_DATABASE_KEYWORD =
      "use database";
  public static final String USE_DATABASE_QUERY_SYNTAX =
      "^(?i)(USE\\sDATABASE\\s[a-zA-Z\\d]+;)$";
  public static final String CREATE_TABLE_KEYWORD =
      "create table";
  public static final String CREATE_TABLE_QUERY_SYNTAX =
      "^(?i)(CREATE\\sTABLE\\s[a-zA-Z\\d]+\\s\\(([a-zA-Z\\d]+\\s(INT|TEXT|FLOAT|BOOLEAN)(\\sPRIMARY KEY|\\sREFERENCES\\s[a-zA-Z\\d]+\\([a-zA-Z\\d]+\\))?(,\\s[a-zA-Z\\d]+\\s(INT|TEXT|FLOAT|BOOLEAN)(\\sPRIMARY KEY|\\sREFERENCES\\s[a-zA-Z\\d]+\\([a-zA-Z\\d]+\\))?)*)\\);)$";
  public static final String DROP_DATABASE_KEYWORD =
      "drop database";
  public static final String DROP_DATABASE_QUERY_SYNTAX =
      "^(?i)(DROP\\sDATABASE\\s[a-zA-Z\\d]+;)$";
  public static final String DROP_TABLE_KEYWORD =
      "drop table";
  public static final String DROP_TABLE_QUERY_SYNTAX =
      "^(?i)(DROP\\sTABLE\\s[a-zA-Z\\d]+;)$";
  public static final String TRUNCATE_TABLE_KEYWORD =
      "truncate table";
  public static final String TRUNCATE_TABLE_QUERY_SYNTAX =
      "^(?i)(TRUNCATE\\sTABLE\\s[a-zA-Z\\d]+;)$";
  public static final String SELECT_KEYWORD =
      "select";
  public static final String SELECT_ALL_KEYWORD =
      "select *";
  public static final String SELECT_ALL_QUERY_SYNTAX =
      "^(?i)(SELECT\\s\\*\\sFROM\\s[a-zA-Z\\d]+;)$";
  public static final String SELECT_DISTINCT_KEYWORD =
      "select distinct";
  public static final String SELECT_DISTINCT_QUERY_SYNTAX =
      "^(?i)(SELECT\\sDISTINCT\\s[a-zA-Z\\d]+\\sFROM\\s[a-zA-Z\\d]+;)$";
  public static final String SELECT_SPECIFIC_KEYWORD =
      "select";
  public static final String SELECT_SPECIFIC_QUERY_SYNTAX =
      "^(?i)(SELECT\\s[a-zA-Z\\d]+(,\\s[a-zA-Z\\d]+)*\\sFROM\\s[a-zA-Z\\d]+;)$";
  public static final String INSERT_KEYWORD =
      "insert";
  public static final String INSERT_QUERY_SYNTAX =
      "^(?i)(INSERT\\sINTO\\s[a-zA-Z\\d]+\\s\\([a-zA-Z\\d]+(,\\s[a-zA-Z\\d]+)*\\)\\sVALUES\\s\\(\\\"?[a-zA-Z\\d\\s~`!@#$^&*-_+=|':;.,?]+\\\"?(,\\s\\\"?[a-zA-Z\\d\\s~`!@#$^&*-_+=|':;.,?]+\\\"?)*\\);)$";
  public static final String START_TRANSACTION_KEYWORD =
      "start transaction";
  public static final String START_TRANSACTION_QUERY =
      "^(?i)START TRANSACTION;$";
  public static final String COMMIT_KEYWORD =
      "commit";
  public static final String COMMIT_QUERY =
      "^(?i)COMMIT;$";
  public static final String ROLLBACK_KEYWORD =
      "rollback";
  public static final String ROLLBACK_QUERY =
      "^(?i)ROLLBACK;$";
}