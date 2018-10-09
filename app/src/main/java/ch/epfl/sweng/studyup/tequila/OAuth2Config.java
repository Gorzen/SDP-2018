package ch.epfl.sweng.studyup.tequila;

/**
 * Configuration for an OAuth2 client.
 *
 * @author Solal Pirelli
 */
public final class OAuth2Config {
    /**
     * These are the scopes your app needs.
     * Unless you wish to fetch data from other EPFL services,
     * `Tequila.profile` is the only scope you need.
     */
    public final String[] scopes;

    /**
     * This is a public value that simply identifies your app.
     */
    public final String clientId;

    /**
     * This value MUST be kept secret.
     * Your Android app MUST NOT handle it.
     * It MUST NOT appear as a constant in your code, even on the server.
     * It MUST NOT be in any git repository.
     * Otherwise, somebody could impersonate your application.
     */
    public final String clientSecret;

    /**
     * This is the URI that Tequila will redirect to after authenticating your users.
     */
    public final String redirectUri;

    public OAuth2Config(String[] scopes, String clientId, String clientSecret, String redirectUri) {
        this.scopes = scopes;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
    }
}
