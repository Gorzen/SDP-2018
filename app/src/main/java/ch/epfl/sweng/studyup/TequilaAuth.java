package ch.epfl.sweng.studyup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import ch.epfl.sweng.studyup.tequila.*;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TequilaAuth extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tequila_auth);

        try {
            // Step 1: String[] scopes, String clientId, String clientSecret, String redirectUri
            OAuth2Config config = readConfig();

            // Step 2: the app requests an authentication token from the "server"
            String codeRequestUrl = AuthClient.createCodeRequestUrl(config);

            // Step 3: we have to connect
            System.out.println(codeRequestUrl);
            /*String redirectUri = read("Go to the above URL, authenticate, then enter the redirect URI");

            String code = AuthClient.extractCode(redirectUri);
            Map<String, String> tokens = AuthServer.fetchTokens(config, code);

            Profile profile = AuthServer.fetchProfile(tokens.get("Tequila.profile"));
            System.out.println(profile);*/
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

    /**
     * Totally secure way to authenticate our app.
     *
     * @return A OAuth2Config that we can use to authenticate
     * @throws IOException
     */
    private static OAuth2Config readConfig() throws IOException {
        return new OAuth2Config(new String[]{"Tequila.profile"}, "05deebf93b675fe41ce9415f@epfl.ch", "875fb85762667d798d3ad90c2d3d2971", "studyup://login");
    }

    /*private static String read(String prompt) throws IOException {
        System.out.print(prompt + ": ");
        return new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
    }*/
}