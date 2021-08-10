package problem3;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class SemanticAnalysis {

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

  private SemanticAnalysisModel getSemanticAnalysisModel(final int articlesContentSetSize,
                                                         final Map<String, Integer> searchQueryKeywordsCounterMap,
                                                         final String termWord,
                                                         final List<SemanticAnalysisModel.TermWordFreq> termWordFreqList) {
    final List<SemanticAnalysisModel.TFIDF> tfidfList = new ArrayList<>();
    for (final Map.Entry<String, Integer> searchQuery : searchQueryKeywordsCounterMap.entrySet()) {
      final double totalDocumentsByDocumentContainingTerm = ((double) articlesContentSetSize / searchQuery.getValue());
      final double log10NTotalDocumentsByDocumentContainingTerm = Math.log10(totalDocumentsByDocumentContainingTerm);
      tfidfList.add(new SemanticAnalysisModel.TFIDF(
          searchQuery.getKey(),
          searchQuery.getValue(),
          totalDocumentsByDocumentContainingTerm,
          log10NTotalDocumentsByDocumentContainingTerm
      ));
    }
    return new SemanticAnalysisModel(
        articlesContentSetSize,
        tfidfList,
        termWord,
        termWordFreqList);
  }

  private void calculateTermWordFreq(final String content,
                                     final String termWord,
                                     final int articleCount,
                                     final List<SemanticAnalysisModel.TermWordFreq> termWordFreqList) {
    int termWordFreq = 0;
    int lastEncounteredIndex = 0;
    while (lastEncounteredIndex != -1) {
      lastEncounteredIndex = content.indexOf(termWord, lastEncounteredIndex);
      if (lastEncounteredIndex != -1) {
        termWordFreq++;
        lastEncounteredIndex += termWord.length();
      }
    }
    if (termWordFreq > 0) {
      final int totalWords = content.split(" ").length;
      final String articleName = "Article: #" + articleCount;
      termWordFreqList.add(new SemanticAnalysisModel.TermWordFreq(articleName, content, totalWords, termWordFreq));
    }
  }

  private void calculateTermFreqAndInverseDocFreq(final String content,
                                                  final String[] searchQueryKeywords,
                                                  final Map<String, Integer> searchQueryKeywordsCounterMap) {
    for (final String searchQuery : searchQueryKeywords) {
      if (content.contains(searchQuery)) {
        if (searchQueryKeywordsCounterMap.containsKey(searchQuery)) {
          searchQueryKeywordsCounterMap.put(searchQuery, searchQueryKeywordsCounterMap.get(searchQuery) + 1);
        } else {
          searchQueryKeywordsCounterMap.put(searchQuery, 1);
        }
      }
    }
  }

  private SemanticAnalysisModel performAnalysis(final MongoCursor<Document> mongoDocuments) {
    final Set<String> articlesContentSet = new HashSet<>();
    while (mongoDocuments.hasNext()) {
      final String content = mongoDocuments.next().getString("content")
          .replaceAll("\\[\\d+ chars]", "")
          .replaceAll("\\\\u\\d{4}", "")
          .replaceAll("(\")|(\\{)|(})|(--)|(\\\\)|(,)|(')|\\(|\\)|(…)", "")
          .trim();
      articlesContentSet.add(content);
    }
    final int articlesContentSetSize = articlesContentSet.size();
    if (articlesContentSetSize > 0) {
      final String[] searchQueryKeywords = new String[]{"Canada", "Moncton", "Toronto"};
      final Map<String, Integer> searchQueryKeywordsCounterMap = new HashMap<>();
      final String termWord = "Canada";
      final List<SemanticAnalysisModel.TermWordFreq> termWordFreqList = new ArrayList<>();
      int articleCount = 0;
      for (final String content : articlesContentSet) {
        articleCount++;
        calculateTermFreqAndInverseDocFreq(content, searchQueryKeywords, searchQueryKeywordsCounterMap);
        calculateTermWordFreq(content, termWord, articleCount, termWordFreqList);
      }
      return getSemanticAnalysisModel(articlesContentSetSize, searchQueryKeywordsCounterMap, termWord, termWordFreqList);
    }
    return null;
  }

  public SemanticAnalysisModel performSemanticAnalysis() {
    try (final MongoClient mongoClient = MongoClients.create(MONGO_CONNECTION_URI)) {
      final MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE_NAME);
      final MongoCollection<Document> mongoCollection = mongoDatabase.getCollection(COLLECTION_NAME);
      final MongoCursor<Document> mongoDocuments = mongoCollection.find().iterator();
      return performAnalysis(mongoDocuments);
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}