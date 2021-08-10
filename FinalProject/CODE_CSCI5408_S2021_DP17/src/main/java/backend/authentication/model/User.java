package backend.authentication.model;

public final class User {

  private final long id;
  private final String userName;
  private final String email;
  private final String password;
  private final String securityQ1Ans;
  private final String securityQ2Ans;
  private final String securityQ3Ans;
  private final String securityQ4Ans;

  public User(final long id,
              final String userName,
              final String email,
              final String password,
              final String securityQ1Ans,
              final String securityQ2Ans,
              final String securityQ3Ans,
              final String securityQ4Ans) {
    this.id = id;
    this.userName = userName;
    this.email = email;
    this.password = password;
    this.securityQ1Ans = securityQ1Ans;
    this.securityQ2Ans = securityQ2Ans;
    this.securityQ3Ans = securityQ3Ans;
    this.securityQ4Ans = securityQ4Ans;
  }

  public User(final String userName,
              final String email,
              final String password,
              final String securityQ1Ans,
              final String securityQ2Ans,
              final String securityQ3Ans,
              final String securityQ4Ans) {
    this(-1,
        userName,
        email,
        password,
        securityQ1Ans,
        securityQ2Ans,
        securityQ3Ans,
        securityQ4Ans);
  }

  public long getId() {
    return id;
  }

  public String getUserName() {
    return userName;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getSecurityQ1Ans() {
    return securityQ1Ans;
  }

  public String getSecurityQ2Ans() {
    return securityQ2Ans;
  }

  public String getSecurityQ3Ans() {
    return securityQ3Ans;
  }

  public String getSecurityQ4Ans() {
    return securityQ4Ans;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", userName='" + userName + '\'' +
        ", email='" + email + '\'' +
        ", password='" + password + '\'' +
        ", securityQ1Ans='" + securityQ1Ans + '\'' +
        ", securityQ2Ans='" + securityQ2Ans + '\'' +
        ", securityQ3Ans='" + securityQ3Ans + '\'' +
        ", securityQ4Ans='" + securityQ4Ans + '\'' +
        '}';
  }
}