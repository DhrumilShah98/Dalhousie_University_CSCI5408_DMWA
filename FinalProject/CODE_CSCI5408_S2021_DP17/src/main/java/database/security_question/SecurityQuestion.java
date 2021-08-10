package database.security_question;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

public final class SecurityQuestion {

  private final Map<Integer, String> securityQuestionsMap = new LinkedHashMap<>();
  private static SecurityQuestion instance;

  private SecurityQuestion() {
    // Required private constructor.
    initSecurityQuestionsMap();
  }

  private void initSecurityQuestionsMap() {
    securityQuestionsMap.clear();
    securityQuestionsMap.put(4, "What was your name of first pet?");
    securityQuestionsMap.put(5, "What is your birth year?");
    securityQuestionsMap.put(6, "What are the last 4 digits of your telephone number?");
    securityQuestionsMap.put(7, "What is your favorite sport?");
  }

  public static SecurityQuestion getInstance() {
    if (instance == null) {
      instance = new SecurityQuestion();
    }
    return instance;
  }

  public Map<Integer, String> getSecurityQuestionsMap() {
    return securityQuestionsMap;
  }

  public int getIndexByQuestion(final String question) {
    for (Map.Entry<Integer, String> entry : securityQuestionsMap.entrySet()) {
      if (entry.getValue().equals(question)) {
        return entry.getKey();
      }
    }
    return -1;
  }

  public String getRandomSecurityQuestion() {
    final int min = 4;
    final int max = 7;
    final int randomQuestionIndex = new Random().nextInt(max - min + 1) + min;
    return securityQuestionsMap.get(randomQuestionIndex);
  }
}