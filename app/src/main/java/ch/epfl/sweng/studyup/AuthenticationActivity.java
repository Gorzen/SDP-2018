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

    private void reportAuthError(String error) {

        Log.e(getString(R.string.auth_error), error);
        Toast.makeText(AuthenticationActivity.this,
                "Unable to authenticate.",
                Toast.LENGTH_SHORT).show();
    }

    private String getTokenFromResponse(String response) throws JSONException {
        JSONObject tokenResponseJSON = new JSONObject(response);

        if (tokenResponseJSON.has("error")) {
            String error = tokenResponseJSON.getString("error");
            reportAuthError(error);
            return null;
        }
        else {
            String token = tokenResponseJSON.getString("access_token");
            return token;
        }
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
        if (profileResponseJSON.has("error")) {
            String error = profileResponseJSON.getString("error");
            reportAuthError(error);
        }
        else {
            String firstName = profileResponseJSON.getString("Firstname");
            String sciperNumber = profileResponseJSON.getString("Sciper");

            TextView profileDataDisplay = findViewById(R.id.profileDataDisplay);
            profileDataDisplay.setText("Welcome, " + firstName + ".\nYour Sciper number is " + sciperNumber + ".");
        }
    }

    private void displayProfileData(String token) throws IOException, JSONException {

        String profileUrl = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/userinfo" + "?access_token=" + token;
        HttpURLConnection profConnection = (HttpURLConnection) new URL(profileUrl).openConnection();
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
            reportAuthError(error);
        }
        else {
            try {
                String token = getToken(code);
                if (token != null) {
                    displayProfileData(token);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
