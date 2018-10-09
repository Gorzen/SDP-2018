package ch.epfl.sweng.studyup.tequila;

/**
 * Client code for Tequila authentication.
 *
 * @author Solal Pirelli
 * modified by us
 */
public final class AuthClient {
    public static String createCodeRequestUrl(OAuth2Config config) {
        return "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/auth" +
                "?response_type=code" +
                "&client_id=" + HttpUtils.urlEncode(config.clientId) +
                "&redirect_uri=" + HttpUtils.urlEncode(config.redirectUri) +
                "&scope=" + String.join(",", config.scopes);
    }

    public static String extractCode(String redirectUri) {
        String marker = "code=";
        return redirectUri.substring(redirectUri.indexOf(marker) + marker.length());
    }
}
