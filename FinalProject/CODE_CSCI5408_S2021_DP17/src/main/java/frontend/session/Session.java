package frontend.session;

import backend.authentication.model.User;

public final class Session {

  private User user = null;
  private static Session instance = null;

  private Session() {
    // Required empty constructor.
  }

  public static Session getInstance() {
    if (instance == null) {
      instance = new Session();
    }
    return instance;
  }

  public void createUserSession(final User user) {
    this.user = user;
  }

  public User getLoggedInUser() {
    return this.user;
  }

  public void destroyUserSession() {
    this.user = null;
  }
}