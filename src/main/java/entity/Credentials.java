package entity;

public class Credentials {

    private final String loginPageUrl;
    private final String login;
    private final String password;

    public Credentials(String loginPageUrl, String login, String password) {
        this.loginPageUrl = loginPageUrl;
        this.login = login;
        this.password = password;
    }

    public String getLoginPageUrl() {
        return loginPageUrl;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Credentials -> [url -> ").append(loginPageUrl)
                .append(", login -> ").append(login)
                .append(", password -> ").append(password)
                .append("]");
        return sb.toString();
    }
}
