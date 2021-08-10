package problem2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SentimentAnalysisModel {

  public enum Polarity {
    POSITIVE,
    NEGATIVE,
    NEUTRAL
  }

  private final int id;

  private final String content;

  private final HashMap<String, Integer> contentFrequencyCount;

  private final Set<String> matchedWords;

  private Polarity polarity;

  private int positiveScore;

  private int negativeScore;

  public SentimentAnalysisModel(final int id,
                                final String content) {
    this.id = id;
    this.content = content;
    this.contentFrequencyCount = new HashMap<>();
    this.matchedWords = new HashSet<>();
    this.polarity = Polarity.NEUTRAL;
    this.positiveScore = 0;
    this.negativeScore = 0;
  }

  public int getId() {
    return this.id;
  }

  public String getContent() {
    return this.content;
  }

  public HashMap<String, Integer> getContentFrequencyCount() {
    return this.contentFrequencyCount;
  }

  public Set<String> getMatchedWords() {
    return this.matchedWords;
  }

  public Polarity getPolarity() {
    return this.polarity;
  }

  public int getPositiveScore() {
    return this.positiveScore;
  }

  public int getNegativeScore() {
    return this.negativeScore;
  }

  private void prepareDescriptionFrequencyCount() {
    final String[] descriptionArr = this.content.split(" ");
    for (final String word : descriptionArr) {
      final String wordLC = word.toLowerCase();
      if (this.contentFrequencyCount.containsKey(wordLC)) {
        this.contentFrequencyCount.put(wordLC,
            this.contentFrequencyCount.get(wordLC) + 1);
      } else {
        this.contentFrequencyCount.put(wordLC, 1);
      }
    }
  }

  public void calculatePolarity(final Set<String> negativeWords,
                                final Set<String> positiveWords) {
    prepareDescriptionFrequencyCount();
    final Set<Map.Entry<String, Integer>> contentFrequencyCountEntries =
        this.contentFrequencyCount.entrySet();
    for (final Map.Entry<String, Integer> contentWordMap :
        contentFrequencyCountEntries) {
      final String contentWord = contentWordMap.getKey();
      if (negativeWords.contains(contentWord)) {
        this.negativeScore += contentWordMap.getValue();
        this.matchedWords.add(contentWord);
      } else if (positiveWords.contains(contentWord)) {
        this.positiveScore += contentWordMap.getValue();
        this.matchedWords.add(contentWord);
      }
    }
    final int finalScore = (-this.negativeScore) + (this.positiveScore);
    if (finalScore > 0) {
      this.polarity = Polarity.POSITIVE;
    } else if (finalScore < 0) {
      this.polarity = Polarity.NEGATIVE;
    } else {
      this.polarity = Polarity.NEUTRAL;
    }
  }
}