package ch.epfl.sweng.studyup;

import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import org.json.*;

import static java.sql.DriverManager.println; //DEBUG TODO

public class AuthenticationActivity extends AppCompatActivity {
    private final String TAG = AuthenticationActivity.class.getSimpleName();
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore fbFirestore;

    private void reportAuthError() {
        Toast.makeText(AuthenticationActivity.this,
                "Unable to authenticate.",
                Toast.LENGTH_SHORT).show();
    }

    private void checkErrorJSON(JSONObject obj) {
        if (obj.has("error")) {
            String error;
            try {
                error = obj.getString("error");
            } catch(JSONException e) {
                e.printStackTrace();
                return;
            }
            reportAuthError();
            Log.e(getString(R.string.auth_error_tequila), error);
        }
    }

    private String getTokenFromResponse(String response) throws JSONException, UnsupportedEncodingException {

        JSONObject tokenResponseJSON = new JSONObject(response);

        if (tokenResponseJSON.has("access_token")) {
            String token = tokenResponseJSON.getString("access_token");
            return URLEncoder.encode(token, "UTF-8");
        }

        checkErrorJSON(tokenResponseJSON);
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
            System.out.println("Welcome, " + firstName + ".\nYour Sciper number is " + sciperNumber + ".");
        }

        checkErrorJSON(profileResponseJSON);
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

        String code;
        try {
            code = authCodeURI.getQueryParameter("code");
        } catch(NullPointerException e) {
            e.printStackTrace();
            return;
        }
        String error = authCodeURI.getQueryParameter("error");

        if (!TextUtils.isEmpty(error)) {
            reportAuthError();
            Log.e(getString(R.string.auth_error_tequila), error);
        }
        else {
            String token;
            try {
                token = getToken(code);
                if (token != null) {
                    displayProfileData(token);
                }
            } catch (Exception e) {
                reportAuthError();
                e.printStackTrace();

                return;
            }

            firebaseAuth = FirebaseAuth.getInstance();
            setupTokenFireBaseAuth(token);
            fbFirestore = FirebaseFirestore.getInstance();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, fbFirestore.collection("users").get().toString());
    }

    /**
     * Method that setup the connection to Firebase Authenticate service using the
     * tequila token
     *
     * @param token
     */
    private void setupTokenFireBaseAuth(String token) {
        firebaseAuth.signInWithCustomToken(token)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d(TAG, getString(R.string.auth_firebase_success));
                            Toast.makeText(AuthenticationActivity.this,
                                    getString(R.string.auth_firebase_success),
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //update firebaseUser in Player
                        } else {
                            Log.w(TAG, getString(R.string.auth_firebase_failure));
                            Toast.makeText(AuthenticationActivity.this,
                                    getString(R.string.auth_firebase_failure),
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}
