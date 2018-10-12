package ch.epfl.sweng.studyup;

import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.*;

public class AuthenticationActivity extends AppCompatActivity {

    private void reportAuthError() {

        Toast.makeText(AuthenticationActivity.this,
                "Unable to authenticate.",
                Toast.LENGTH_SHORT).show();
    }

    private String getTokenFromResponse(String response) throws JSONException {

        JSONObject tokenResponseJSON = new JSONObject(response);

        if (tokenResponseJSON.has("access_token")) {
            String token = tokenResponseJSON.getString("access_token");
            return token;
        }
        if (tokenResponseJSON.has("error")) {
            String error = tokenResponseJSON.getString("error");
            reportAuthError();
            Log.e(getString(R.string.auth_error), error);
        }
        return null;
    }

    private String getToken(String code) throws IOException, JSONException {

        String tokenURL = "https://studyup-authenticate.herokuapp.com/getToken" + "?code=" + code;
        HttpURLConnection connection = (HttpURLConnection) new URL(tokenURL).openConnection();
        InputStream stream = connection.getInputStream();
        String tokenResponse = new Scanner(stream).useDelimiter("\\A").next();

        return getTokenFromResponse(tokenResponse);
    }

    private void displayProfileDataFromResponse(String response) throws JSONException {

        JSONObject profileResponseJSON = new JSONObject(response);

        if (profileResponseJSON.has("Firstname") && profileResponseJSON.has("Sciper")) {
            String firstName = profileResponseJSON.getString("Firstname");
            String sciperNumber = profileResponseJSON.getString("Sciper");

            TextView profileDataDisplay = findViewById(R.id.profileDataDisplay);
            profileDataDisplay.setText("Welcome, " + firstName + ".\nYour Sciper number is " + sciperNumber + ".");
        }
        if (profileResponseJSON.has("error")) {
            String error = profileResponseJSON.getString("error");
            reportAuthError();
            Log.e(getString(R.string.auth_error), error);
        }
    }

    private void displayProfileData(String token) throws IOException, JSONException {

        System.setProperty("http.agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:56.0) Gecko/20100101 Firefox/56.0");

        String profileUrl = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/userinfo" + "?access_token=" + token;
        HttpURLConnection profConnection = (HttpURLConnection) new URL(profileUrl).openConnection();

        int profConnectionResponseCode = profConnection.getResponseCode();
        if (profConnectionResponseCode >= 400) {
            System.out.println("ERROR, SERVER RESPONSE CODE: " + profConnectionResponseCode);
            InputStream errorStream = profConnection.getInputStream();
            String errorResponse = new Scanner(errorStream).useDelimiter("\\A").next();
            System.out.println(errorResponse);
        }

        InputStream profStream = profConnection.getInputStream();
        String profileResponse = new Scanner(profStream).useDelimiter("\\A").next();

        displayProfileDataFromResponse(profileResponse);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Uri authCodeURI = getIntent().getData();

        String code = authCodeURI.getQueryParameter("code");
        String error = authCodeURI.getQueryParameter("error");

        if (!TextUtils.isEmpty(error)) {
            reportAuthError();
            Log.e(getString(R.string.auth_error), error);
        }
        else {
            try {
                String token = getToken(code);
                if (token != null) {
                    displayProfileData(token);
                }
            } catch (Exception e) {
                reportAuthError();
                e.printStackTrace();
            }
        }
    }
}
