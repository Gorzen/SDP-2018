package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;
import java.util.Map;

import ch.epfl.sweng.studyup.tequila.*;


public class TequilaAuth extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tequila_auth);

        try {
            // Step 1: String[] scopes, String clientId, String clientSecret, String redirectUri
            OAuth2Config config = readConfig();

            // Step 2: the app requests an authentication token from the "server"
            String codeRequestUrl = createCodeRequestUrl(config);

            // Step 3: we have to connect
            System.out.println(codeRequestUrl); // Debug
            // Open browser from app with the login to Tequila, then redirect back to the app
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(codeRequestUrl));
            startActivity(browserIntent);

            // TODO: Extract the Uri from the request
            String redirectUri = "";// read("Go to the above URL, authenticate, then enter the redirect URI");

            String code = extractCode(redirectUri);
            Map<String, String> tokens = AuthServer.fetchTokens(config, code);

            Profile profile = AuthServer.fetchProfile(tokens.get("Tequila.profile"));
            System.out.println(profile);
        } catch (IOException e) {
            //
        }
    }

    /*public static void main(String[] args) throws IOException {
        OAuth2Config config = readConfig();
        String codeRequestUrl = AuthClient.createCodeRequestUrl(config);

        System.out.println(codeRequestUrl);
        String redirectUri = read("Go to the above URL, authenticate, then enter the redirect URI");

        String code = AuthClient.extractCode(redirectUri);
        Map<String, String> tokens = AuthServer.fetchTokens(config, code);

        Profile profile = AuthServer.fetchProfile(tokens.get("Tequila.profile"));
        System.out.println(profile);
    }*/

    // TODO: Setup a real, secured way to authenticate!
    /**
     * Totally secure way to authenticate our app.
     *
     * @return OAuth2Config
     * @throws IOException
     */
    private static OAuth2Config readConfig() throws IOException {
        return new OAuth2Config(new String[]{"Tequila.profile"}, "05deebf93b675fe41ce9415f@epfl.ch", "875fb85762667d798d3ad90c2d3d2971", "studyup://login");
    }

    /**
     * Takes a valid OAuth2Config and returns the URL to get a code
     *
     * @param config A valid OAuth2Config
     * @return Code URL
     */
    public static String createCodeRequestUrl(OAuth2Config config) {
        String scopesConcat = new String();
        for(int i = 0; i < config.scopes.length; ++i) {
            scopesConcat = scopesConcat.concat(","+config.scopes[i]);
        }
        return "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/auth" +
                "?response_type=code" +
                "&client_id=" + HttpUtils.urlEncode(config.clientId) +
                "&redirect_uri=" + HttpUtils.urlEncode(config.redirectUri) +
                "&scope=" + scopesConcat;
    }

    /**
     * Extract the token from a redirectUri
     * @param redirectUri The redirectUri with the code in it
     * @return token
     * @throws IllegalArgumentException If there is no code
     */
    private static String extractCode(String redirectUri) throws IllegalArgumentException {
        if(redirectUri != null && !redirectUri.isEmpty()) {
            String marker = "code=";
            if(redirectUri.contains(marker)) {
                return redirectUri.substring(redirectUri.indexOf(marker) + marker.length());
            } else {
                throw new IllegalArgumentException("Error: redirectUri not valid");
            }
        } else {
            throw new IllegalArgumentException("Error: redirectUri cannot be null nor empty");
        }
    }
}