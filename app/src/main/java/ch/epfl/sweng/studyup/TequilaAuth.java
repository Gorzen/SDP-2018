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
            String codeRequestUrl, redirectUri;

            OAuth2Config config = readConfig();
            codeRequestUrl = AuthClient.createCodeRequestUrl(config);

            System.out.println(codeRequestUrl);
            try {
                redirectUri = read("Go to the above URL, authenticate, then enter the redirect URI");

                String code = AuthClient.extractCode(redirectUri);
                Map<String, String> tokens = AuthServer.fetchTokens(config, code);

                Profile profile = AuthServer.fetchProfile(tokens.get("Tequila.profile"));
                System.out.println(profile);
            } catch(IOException e) {
                System.out.println("Exception occurred: OAuth2Config readConfig()");
            }


        } catch(IOException e) {
            System.out.println("Exception occurred: OAuth2Config readConfig()");
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

    private static OAuth2Config readConfig() throws IOException {
        return new OAuth2Config(new String[]{"Tequila.profile"}, read("Client ID"), read("Client secret"), read("Redirect URI"));
    }

    private static String read(String prompt) throws IOException {
        System.out.print(prompt + ": ");
        return new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
    }
}