package frontend.print;

public final class CSCI5408DP17Printer {

  private static CSCI5408DP17Printer instance;

  private CSCI5408DP17Printer() {
    // Required private constructor.
  }

  public static CSCI5408DP17Printer getInstance() {
    if (instance == null) {
      instance = new CSCI5408DP17Printer();
    }
    return instance;
  }

  public final void printScreenTitle(final String screenTitle) {
    System.out.println("**************************************************");
    System.out.println(screenTitle);
    System.out.println("**************************************************");
  }

  public final void printContent(final String content) {
    System.out.println(content);
  }

  public final void printBeautifyContent(final String beautifyPattern,
                                         final Object... args) {
    System.out.printf(beautifyPattern, args);
  }
}