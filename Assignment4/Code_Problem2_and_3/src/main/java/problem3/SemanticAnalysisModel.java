package problem3;

import java.util.List;

public final class SemanticAnalysisModel {

  public static final class TFIDF {

    private final String searchQuery;

    private final int documentContainingTerm;

    private final double totalDocumentsByDocumentContainingTerm;

    private final double log10NTotalDocumentsByDocumentContainingTerm;

    public TFIDF(final String searchQuery,
                 final int documentContainingTerm,
                 final double totalDocumentsByDocumentContainingTerm,
                 final double log10NTotalDocumentsByDocumentContainingTerm) {
      this.searchQuery = searchQuery;
      this.documentContainingTerm = documentContainingTerm;
      this.totalDocumentsByDocumentContainingTerm = totalDocumentsByDocumentContainingTerm;
      this.log10NTotalDocumentsByDocumentContainingTerm = log10NTotalDocumentsByDocumentContainingTerm;
    }

    public String getSearchQuery() {
      return this.searchQuery;
    }

    public int getDocumentContainingTerm() {
      return this.documentContainingTerm;
    }

    public double getTotalDocumentsByDocumentContainingTerm() {
      return this.totalDocumentsByDocumentContainingTerm;
    }

    public double getLog10NTotalDocumentsByDocumentContainingTerm() {
      return this.log10NTotalDocumentsByDocumentContainingTerm;
    }
  }

  public static final class TermWordFreq {

    private final String articleName;

    private final String articleContent;

    private final int totalWordsCount;

    private final int frequency;

    public TermWordFreq(final String articleName,
                        final String articleContent,
                        final int totalWordsCount,
                        final int frequency) {
      this.articleName = articleName;
      this.articleContent = articleContent;
      this.totalWordsCount = totalWordsCount;
      this.frequency = frequency;
    }

    public String getArticleName() {
      return this.articleName;
    }

    public String getArticleContent() {
      return this.articleContent;
    }

    public int getTotalWordsCount() {
      return this.totalWordsCount;
    }

    public int getFrequency() {
      return this.frequency;
    }
  }

  private final int totalDocuments;

  private final List<TFIDF> tfidfList;

  private final String term;

  private final List<TermWordFreq> termWordFreqList;

  public SemanticAnalysisModel(final int totalDocuments,
                               final List<TFIDF> tfidfList,
                               final String term,
                               final List<TermWordFreq> termWordFreqList) {
    this.totalDocuments = totalDocuments;
    this.tfidfList = tfidfList;
    this.term = term;
    this.termWordFreqList = termWordFreqList;
  }

  public int getTotalDocuments() {
    return this.totalDocuments;
  }

  public List<TFIDF> getTfidfList() {
    return this.tfidfList;
  }

  public String getTerm() {
    return this.term;
  }

  public List<TermWordFreq> getTermWordFreqList() {
    return this.termWordFreqList;
  }
}