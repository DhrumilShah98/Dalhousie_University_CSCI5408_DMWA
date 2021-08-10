import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class NewsAPIFiltrationEngine {
    private static final String SEPARATOR_REPLACEMENT;
    private static final String SEPARATOR_SPLIT;
    private static final String EMPTY_STRING;
    private static final String NEWS_DATA_DIRECTORY_NAME;
    private static final String MONGO_USER_NAME;
    private static final String MONGO_PASSWORD;
    private static final String MONGO_HOST_NAME;
    private static final String DATABASE_NAME;
    private static final String COLLECTION_NAME;
    private static final String MONGO_CONNECTION_URI;

    static {
        SEPARATOR_REPLACEMENT = "}@@@@@@@@@{";
        SEPARATOR_SPLIT = "@@@@@@@@@";
        EMPTY_STRING = "";
        NEWS_DATA_DIRECTORY_NAME = "./newsData/";
        MONGO_USER_NAME = "<MONGODB_USER_NAME>";
        MONGO_PASSWORD = "<MONGODB_PASSWORD>";
        MONGO_HOST_NAME = "<MONGODB_HOST_NAME>";
        DATABASE_NAME = "<MONGODB_DATABASE_NAME>";
        COLLECTION_NAME = "<MONGODB_COLLECTION_NAME>";
        MONGO_CONNECTION_URI = "mongodb+srv://" + MONGO_USER_NAME + ":" + MONGO_PASSWORD + "@" + MONGO_HOST_NAME + "/" + DATABASE_NAME + "?retryWrites=true&w=majority";
    }

    private File[] readAllFileNames() {
        final File newsDataFolder = new File(NEWS_DATA_DIRECTORY_NAME);
        return newsDataFolder.listFiles();
    }

    private String filterArticleContent(final String articleContent) {
        String filteredArticleContent;
        final String emojisFilter = "[^\\p{L}\\p{N}\\p{P}\\p{Z}]";
        final String urlToImageFilter = "(\"urlToImage\":(\"http.*?\"|null|\"null\"|\"\"),)";
        final String urlFilter = "(\"url\":(\"http.*?\"|null|\"null\"|\"\"),)";
        final String authorFilter = "(\"author\":(\"http.*?\"|null|\"null\"|\"\"),)";
        final String idFilter = "(,\"id\":(null|\"null\"|\"\"))";
        final String generalFilter = "(\\\\[ntr])|( )|(<[^>]*>)";
        final String separatorFilter = "},{";
        filteredArticleContent = articleContent.replaceAll(emojisFilter, EMPTY_STRING);
        filteredArticleContent = filteredArticleContent.replaceAll(urlToImageFilter, EMPTY_STRING);
        filteredArticleContent = filteredArticleContent.replaceAll(urlFilter, EMPTY_STRING);
        filteredArticleContent = filteredArticleContent.replaceAll(authorFilter, EMPTY_STRING);
        filteredArticleContent = filteredArticleContent.replaceAll(idFilter, EMPTY_STRING);
        filteredArticleContent = filteredArticleContent.replaceAll(generalFilter, EMPTY_STRING);
        filteredArticleContent = filteredArticleContent.substring(1, filteredArticleContent.length() - 1);
        filteredArticleContent = filteredArticleContent.replace(separatorFilter, SEPARATOR_REPLACEMENT);
        return filteredArticleContent;
    }

    private void readAndFilterArticles(final File[] allNewsFiles, final List<Document> mongoNewsDocuments) {
        int totalArticlesRead = 0;
        for (final File newsFile : allNewsFiles) {
            try {
                final String newsFileContent = Files.readString(Paths.get(newsFile.getPath())).trim();
                final String[] filteredArticlesList = filterArticleContent(newsFileContent).split(SEPARATOR_SPLIT);
                for (String article : filteredArticlesList) {
                    final Document articleDoc = Document.parse(article);
                    mongoNewsDocuments.add(articleDoc);
                    totalArticlesRead++;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
        System.out.println("Total articles read and filtered - " + totalArticlesRead);
    }

    public void filterNewsData() {
        final File[] allNewsFiles = readAllFileNames();
        if (allNewsFiles != null && allNewsFiles.length != 0) {
            final List<Document> mongoNewsDocuments = new ArrayList<>();
            System.out.println("Filtering all articles...");
            readAndFilterArticles(allNewsFiles, mongoNewsDocuments);
            try (MongoClient mongoNewsCluster0Client = MongoClients.create(MONGO_CONNECTION_URI)) {
                final MongoCollection<Document> mongoNewsCollection = mongoNewsCluster0Client.getDatabase(DATABASE_NAME).getCollection(COLLECTION_NAME);
                mongoNewsCollection.insertMany(mongoNewsDocuments);
                System.out.println("Articles stored successfully.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }
}