package backend.query_processor.model;

public final class QueryProcessorResponse {

  private final boolean success;
  private final String result;

  public QueryProcessorResponse(final boolean success,
                                final String result) {
    this.success = success;
    this.result = result;
  }

  public boolean isSuccess() {
    return success;
  }

  public String getResult() {
    return result;
  }
}