import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

public final class NewsAPIExtractionEngine {
    private static final String NEWS_DATA_DIRECTORY_NAME;
    private static final String NEWS_API_URL;
    private static final String NEWS_API_KEY;
    private static final int TOTAL_ARTICLES_ALLOWED_FOR_EACH_CATEGORY;
    private static final int TOTAL_ARTICLES_ALLOWED_IN_EACH_FILE;
    private static final String[] ALL_CATEGORY_KEYWORDS;

    static {
        NEWS_DATA_DIRECTORY_NAME = "./newsData/";
        NEWS_API_URL = "https://newsapi.org/v2/everything";
        NEWS_API_KEY = "<API_KEY>";
        TOTAL_ARTICLES_ALLOWED_FOR_EACH_CATEGORY = 100;
        TOTAL_ARTICLES_ALLOWED_IN_EACH_FILE = 5;
        ALL_CATEGORY_KEYWORDS = new String[]{"Canada", "University", "Dalhousie", "Halifax", "Canada Education", "Moncton", "Toronto"};
    }

    private String fetchNews(final String category) {
        try {
            final String queryCategory = category.replaceAll(" ", "%20");
            final String newsAPIURL = NEWS_API_URL + "?q=" + queryCategory + "&language=en&pageSize=" + TOTAL_ARTICLES_ALLOWED_FOR_EACH_CATEGORY;
            final HttpRequest httpRequest = HttpRequest
                    .newBuilder(URI.create(newsAPIURL))
                    .header("X-Api-Key", NEWS_API_KEY)
                    .timeout(Duration.of(30, ChronoUnit.SECONDS))
                    .GET()
                    .build();
            final HttpClient httpClient = HttpClient.newBuilder().build();
            final HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            return httpResponse.body();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    private void storeNews(final String category, final String newsArticleJSON) {
        final String categoryFileName = category.replaceAll(" ", "").toLowerCase();
        final Path path = Paths.get(NEWS_DATA_DIRECTORY_NAME);
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            return;
        }
        final String newsDataFileName = NEWS_DATA_DIRECTORY_NAME + categoryFileName + "_" + System.currentTimeMillis() + ".json";
        try (FileWriter fileWriter = new FileWriter(newsDataFileName, StandardCharsets.UTF_8)) {
            fileWriter.write(newsArticleJSON);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    private void prepareAndStoreNews(final String category, final String newsJSON) {
        final JSONObject newsJSONObject = new JSONObject(newsJSON);
        final JSONArray newsArticlesArray = newsJSONObject.getJSONArray("articles");
        final StringBuilder articlesStringBuilder = new StringBuilder();
        int totalArticlesInEachFile = 0;
        for (int currentArticleIndex = 0; currentArticleIndex < newsArticlesArray.length(); ++currentArticleIndex) {
            articlesStringBuilder.append(newsArticlesArray.getJSONObject(currentArticleIndex)).append(",");
            totalArticlesInEachFile++;
            if (totalArticlesInEachFile == TOTAL_ARTICLES_ALLOWED_IN_EACH_FILE) {
                articlesStringBuilder.setLength(articlesStringBuilder.length() - 1);
                String articlesInEachFileStringBuilder = "[" + articlesStringBuilder + "]";
                storeNews(category, articlesInEachFileStringBuilder);
                totalArticlesInEachFile = 0;
                articlesStringBuilder.setLength(0);
            }
        }
        if (totalArticlesInEachFile > 0) {
            articlesStringBuilder.setLength(articlesStringBuilder.length() - 1);
            String articlesInEachFileStringBuilder = "[" + articlesStringBuilder + "]";
            storeNews(category, articlesInEachFileStringBuilder);
            articlesStringBuilder.setLength(0);
        }
    }

    public void extractNewsData() {
        for (String category : ALL_CATEGORY_KEYWORDS) {
            System.out.println("Fetching and storing news for category - " + category + ".");
            final String newsJSON = fetchNews(category);
            if (newsJSON != null) {
                prepareAndStoreNews(category, newsJSON);
            }
        }
    }
}