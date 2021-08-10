package problem2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class TestSentimentAnalysis {
  public static void main(String[] args) {

    // Add the below line in VM Options to establish the connection with MongoDB.
    // -Djdk.tls.client.protocols=TLSv1.2

    // Print only MongoDB Severe logs
    System.setProperty("java.net.preferIPv4Stack", "true");
    Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

    // Sentiment analysis model list
    System.out.println("Sentiment analysis in progress...");
    final List<SentimentAnalysisModel> sentimentAnalysisModelList = new SentimentAnalysis().performSentimentAnalysis();
    final String problem2OutputFile =
        "src" + File.separator +
            "main" + File.separator +
            "java" + File.separator +
            "problem2" + File.separator +
            "problem_2_output.txt";

    // Print in file
    try (final FileWriter fileWriter = new FileWriter(problem2OutputFile, true)) {
      for (SentimentAnalysisModel news : sentimentAnalysisModelList) {
        fileWriter.append("News Article Id: ").append(String.valueOf(news.getId())).append("\n");
        fileWriter.append("News Content: ").append(news.getContent()).append("\n");
        fileWriter.append("Word Bag: ").append(String.valueOf(news.getContentFrequencyCount())).append("\n");
        fileWriter.append("Matched Words: ").append(String.valueOf(news.getMatchedWords())).append("\n");
        fileWriter.append("Positive Score: +").append(String.valueOf(news.getPositiveScore())).append("\n");
        fileWriter.append("Negative Score: -").append(String.valueOf(news.getNegativeScore())).append("\n");
        fileWriter.append("Polarity: ").append(String.valueOf(news.getPolarity())).append("\n\n");
      }

      // Output printed
      System.out.println("Output in file: " + problem2OutputFile);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }
}