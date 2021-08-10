import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class NewsAPIWordCounterEngine {
    private static final String NEWS_DATA_DIRECTORY_NAME;
    private static final String[] ALL_CATEGORY_KEYWORDS;

    static {
        NEWS_DATA_DIRECTORY_NAME = "./newsData/";
        ALL_CATEGORY_KEYWORDS = new String[]{"Canada", "Nova Scotia", "education", "higher", "learning", "city", "accommodation", "price"};
    }

    private Map<String, Integer> initWordCounterMap() {
        final Map<String, Integer> wordCounterMap = new HashMap<>();
        final Integer initialWordCount = 0;
        for (String categoryKeyword : ALL_CATEGORY_KEYWORDS) {
            wordCounterMap.put(categoryKeyword, initialWordCount);
        }
        return wordCounterMap;
    }

    private File[] readAllFileNames() {
        final File newsDataFolder = new File(NEWS_DATA_DIRECTORY_NAME);
        return newsDataFolder.listFiles();
    }

    private String map(final String newsFileContent) {
        final Matcher titleMatcher = Pattern.compile("(\"title\":\".*?\",)").matcher(newsFileContent);
        final Matcher contentMatcher = Pattern.compile("(\"content\":\".*?\"},*)").matcher(newsFileContent);
        final StringBuilder mappedStringBuilder = new StringBuilder();
        while (titleMatcher.find()) {
            mappedStringBuilder.append(titleMatcher.group());
        }
        while (contentMatcher.find()) {
            mappedStringBuilder.append(contentMatcher.group());
        }
        return mappedStringBuilder.toString();
    }

    private void reduce(final String mappedString, final Map<String, Integer> wordCounterMap) {
        for (String categoryKeyword : ALL_CATEGORY_KEYWORDS) {
            int lastEncounteredIndex = 0;
            while (lastEncounteredIndex != -1) {
                lastEncounteredIndex = mappedString.indexOf(categoryKeyword, lastEncounteredIndex);
                if (lastEncounteredIndex != -1) {
                    wordCounterMap.put(categoryKeyword, wordCounterMap.get(categoryKeyword) + 1);
                    lastEncounteredIndex += categoryKeyword.length();
                }
            }
        }
    }

    private void performMapReduce(final File[] allNewsFiles, final Map<String, Integer> wordCounterMap) {
        for (final File newsFile : allNewsFiles) {
            try {
                final String newsFileContent = Files.readString(Paths.get(newsFile.getPath())).trim();
                final String mappedString = map(newsFileContent);
                reduce(mappedString, wordCounterMap);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
            }
        }
    }

    public Map<String, Integer> wordCountNewsData() {
        final Map<String, Integer> wordCounterMap = initWordCounterMap();
        final File[] allNewsFiles = readAllFileNames();
        if (wordCounterMap.isEmpty() || allNewsFiles != null && allNewsFiles.length != 0) {
            performMapReduce(allNewsFiles, wordCounterMap);
        }
        return wordCounterMap;
    }
}