package backend.authentication.session;

import backend.authentication.model.User;

public final class BackendSession {

  private static User loggedInUser = null;

  private BackendSession() {
    // Required empty constructor.
  }

  public static User getLoggedInUser() {
    return BackendSession.loggedInUser;
  }

  public static void setLoggedInUser(final User user) {
    BackendSession.loggedInUser = user;
  }
}