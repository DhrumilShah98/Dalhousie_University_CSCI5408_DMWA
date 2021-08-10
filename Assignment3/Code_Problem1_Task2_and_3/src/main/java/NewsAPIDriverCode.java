import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class NewsAPIDriverCode {
    public static void main(String[] args) {
        try {
//            Add the below line in VM Options to establish the connection with MongoDB and insert Documents
//            -Djdk.tls.client.protocols=TLSv1.2

            final NewsAPIExtractionEngine extractionEngine = new NewsAPIExtractionEngine();
            extractionEngine.extractNewsData();

            System.setProperty("java.net.preferIPv4Stack", "true");
            Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

            final NewsAPIFiltrationEngine filtrationEngine = new NewsAPIFiltrationEngine();
            filtrationEngine.filterNewsData();

            final NewsAPIWordCounterEngine wordCounterEngine = new NewsAPIWordCounterEngine();
            final Map<String, Integer> wordCounterMap = wordCounterEngine.wordCountNewsData();

            System.out.printf("%n%-15s%-10s%n", "Word", "Frequency");
            for (Map.Entry<String, Integer> wordFrequency : wordCounterMap.entrySet()) {
                System.out.printf("%-15s%-10s%n", wordFrequency.getKey(), wordFrequency.getValue());
            }
            int maxWordCount = -1;
            String maxWord = null;
            int minWordCount = -1;
            String minWord = null;
            for (Map.Entry<String, Integer> wordFrequency : wordCounterMap.entrySet()) {
                if (minWordCount == -1 && maxWordCount == -1) {
                    minWord = wordFrequency.getKey();
                    maxWord = wordFrequency.getKey();
                    minWordCount = wordFrequency.getValue();
                    maxWordCount = wordFrequency.getValue();
                } else {
                    if (wordFrequency.getValue() > maxWordCount) {
                        maxWord = wordFrequency.getKey();
                        maxWordCount = wordFrequency.getValue();
                    }
                    if (wordFrequency.getValue() < minWordCount) {
                        minWord = wordFrequency.getKey();
                        minWordCount = wordFrequency.getValue();
                    }
                }
            }
            System.out.println("\nThe word having the highest frequency is \"" + maxWord + "\", which is \"" + maxWordCount + "\".");
            System.out.println("The word having the lowest frequency is \"" + minWord + "\", which is \"" + minWordCount + "\".");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}