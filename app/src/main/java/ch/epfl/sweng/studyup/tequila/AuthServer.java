package ch.epfl.sweng.studyup.tequila;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface for authentication on a server.
 *
 * @author Solal Pirelli
 */
public final class AuthServer {
    public static Map<String, String> fetchTokens(OAuth2Config config, String code) throws IOException {
        Map<String, String> result = new HashMap<>();
        for (String scope : config.scopes) {
            String url = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/token" +
                    "?client_id=" + HttpUtils.urlEncode(config.clientId) +
                    "&client_secret=" + HttpUtils.urlEncode(config.clientSecret) +
                    "&redirect_uri=" + HttpUtils.urlEncode(config.redirectUri) +
                    "&grant_type=authorization_code" +
                    "&code=" + code +
                    "&scope=" + scope;
            JsonTokenContainer container = HttpUtils.fetch(url, JsonTokenContainer.class);

            if (container.error != null) {
                throw new IOException("Error from Tequila: " + container.error);
            }

            result.put(scope, container.token);
        }

        return result;
    }

    public static Profile fetchProfile(String token) throws IOException {
        String url = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/userinfo" +
                "?access_token=" + HttpUtils.urlEncode(token);
        JsonProfile profile = HttpUtils.fetch(url, JsonProfile.class);

        if (profile.error != null) {
            throw new IOException("Error from Tequila:" + profile.error);
        }

        return new Profile(profile.sciper, profile.gaspar, profile.email, profile.firstNames, profile.lastNames);
    }


    private static final class JsonTokenContainer {
        @SerializedName("error")
        public String error;

        @SerializedName("access_token")
        public String token;
    }

    private static final class JsonProfile {
        @SerializedName("error")
        public String error;

        @SerializedName("Firstname")
        public String firstNames;

        @SerializedName("Name")
        public String lastNames;

        @SerializedName("Email")
        public String email;

        @SerializedName("Sciper")
        public String sciper;

        @SerializedName("Username")
        public String gaspar;
    }
}
