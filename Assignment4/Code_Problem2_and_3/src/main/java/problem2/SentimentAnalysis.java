package problem2;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class SentimentAnalysis {

  private static final String MONGO_USER_NAME;
  private static final String MONGO_PASSWORD;
  private static final String MONGO_HOST_NAME;
  private static final String DATABASE_NAME;
  private static final String COLLECTION_NAME;
  private static final String MONGO_CONNECTION_URI;

  static {
    MONGO_USER_NAME = "<MONGODB_USER_NAME>";
    MONGO_PASSWORD = "<MONGODB_PASSWORD>";
    MONGO_HOST_NAME = "<MONGODB_HOST_NAME>";
    DATABASE_NAME = "<MONGODB_DATABASE_NAME>";
    COLLECTION_NAME = "<MONGODB_COLLECTION_NAME>";
    MONGO_CONNECTION_URI = "mongodb+srv://" + MONGO_USER_NAME + ":" +
        MONGO_PASSWORD + "@" +
        MONGO_HOST_NAME + "/" +
        DATABASE_NAME +
        "?retryWrites=true&w=majority";
  }

  private Set<String> preparePositiveSentimentWordsSet() {
    // https://gist.github.com/mkulakowski2/4289437
    final String positiveWordsFile = "sentiment_words" + File.separator + "positive_words.txt";
    final Set<String> positiveWordsSet = new HashSet<>();
    try (final FileReader posFileReader = new FileReader(positiveWordsFile);
         final BufferedReader posBufferedReader = new BufferedReader(posFileReader)) {
      String positiveWord;
      while ((positiveWord = posBufferedReader.readLine()) != null) {
        positiveWordsSet.add(positiveWord.toLowerCase());
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return positiveWordsSet;
  }

  private Set<String> prepareNegativeSentimentWordsSet() {
    // https://gist.github.com/mkulakowski2/4289441
    final String negativeWordsFile = "sentiment_words" + File.separator + "negative_words.txt";
    final Set<String> negativeWordsSet = new HashSet<>();
    try (final FileReader negFileReader = new FileReader(negativeWordsFile);
         final BufferedReader negBufferedReader = new BufferedReader(negFileReader)) {
      String negativeWord;
      while ((negativeWord = negBufferedReader.readLine()) != null) {
        negativeWordsSet.add(negativeWord.toLowerCase());
      }
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return negativeWordsSet;
  }

  private void prepareModel(final MongoCursor<Document> mongoDocuments,
                            final List<SentimentAnalysisModel> sentimentAnalysisModelList) {
    final Set<String> articlesContentSet = new LinkedHashSet<>();
    while (mongoDocuments.hasNext()) {
      final String content = mongoDocuments.next().getString("content")
          .replaceAll("\\[\\d+ chars]", "")
          .replaceAll("\\\\u\\d{4}", "")
          .replaceAll("(\")|(\\{)|(})|(--)|(\\\\)|(,)|(')|\\(|\\)|(…)", "")
          .trim();
      articlesContentSet.add(content);
    }
    for (final String content : articlesContentSet) {
      sentimentAnalysisModelList.add(new SentimentAnalysisModel(sentimentAnalysisModelList.size() + 1, content));
    }
  }

  private void performAnalysis(final MongoCursor<Document> mongoDocuments,
                               final List<SentimentAnalysisModel> sentimentAnalysisModelList) {
    prepareModel(mongoDocuments, sentimentAnalysisModelList);
    final Set<String> positiveSentimentWordsSet = preparePositiveSentimentWordsSet();
    final Set<String> negativeSentimentWordsSet = prepareNegativeSentimentWordsSet();
    sentimentAnalysisModelList.forEach(entry ->
        entry.calculatePolarity(negativeSentimentWordsSet, positiveSentimentWordsSet));
  }

  public List<SentimentAnalysisModel> performSentimentAnalysis() {
    final List<SentimentAnalysisModel> sentimentAnalysisModelList = new ArrayList<>();
    try (final MongoClient mongoClient = MongoClients.create(MONGO_CONNECTION_URI)) {
      final MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE_NAME);
      final MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION_NAME);
      final MongoCursor<Document> mongoDocuments = mongoCollection.find().iterator();
      performAnalysis(mongoDocuments, sentimentAnalysisModelList);
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return sentimentAnalysisModelList;
  }
}