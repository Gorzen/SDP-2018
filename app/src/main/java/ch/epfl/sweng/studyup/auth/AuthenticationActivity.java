package ch.epfl.sweng.studyup.auth;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.LoginActivity;
import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.utils.Utils;

/**
 * AuthenticationActivity
 *
 * Code used in the activity_authentication.
 */
public class AuthenticationActivity extends AppCompatActivity {
    private final String TAG = AuthenticationActivity.class.getSimpleName();

    public void reportAuthError() {
        Toast.makeText(AuthenticationActivity.this,
                "Unable to authenticate.",
                Toast.LENGTH_SHORT).show();
    }

    public void runAuthentication(String code, boolean isRealRequest) {

        String token;
        if(isRealRequest) {
            token = Authenticator.getToken(code);
        } else {
            token = "Non-null token.";
        }

        if (token != null) {
            String greeting;
            if(isRealRequest) {
                greeting = Authenticator.getGreeting(token);
            } else {
                greeting = R.string.initial_greeting_1+"\n"+R.string.initial_greeting_2;
            }

                if (greeting != null) {

                    if (!getIntent().getBooleanExtra("instrumentationTest", false)) {
                        Firestore.get().getAndSetUserData(
                                Player.get().getSciper(),
                                Player.get().getFirstName(),
                                Player.get().getLastName());
                    }

                    Utils.waitAndTag(Utils.TIME_TO_WAIT_FOR_LOGIN, TAG);

                    Intent initActivity;
                    if (Player.get().getRole()) {
                        initActivity = new Intent(AuthenticationActivity.this, AddQuestionActivity.class);
                    } else {
                        initActivity = new Intent(AuthenticationActivity.this, MainActivity.class);
                    }
                    
                    initActivity.putExtra(
                        getString(R.string.post_login_message_value),
                        getString(R.string.post_login_message_value)
                    );

                    startActivity(initActivity);
                }

            if (greeting != null) {
                TextView profileDataDisplay = findViewById(R.id.profileDataDisplay);
                if(isRealRequest) profileDataDisplay.setText(greeting);
                return;
            }
        }
        reportAuthError();
    }

    /**
     * Function used to store the Player's data to the cache (used to persist the login of the user)
     */
    public void putPlayerDataToCache() {
        FileCacher<String[]> persistLogin = new FileCacher<>(this, Utils.PERSIST_LOGIN_FILENAME);
        String[] cachedData = new String[4];

        cachedData[0] = String.valueOf(Player.get().getSciper());
        cachedData[1] = Player.get().getFirstName();
        cachedData[2] = Player.get().getLastName();
        if (Player.get().getRole()) {
            cachedData[3] = Utils.FB_ROLES_T;
        } else {
            cachedData[3] = Utils.FB_ROLES_S;
        }

        try {
            persistLogin.writeCache(cachedData);
        } catch (IOException e) {
            Log.d(TAG, "The login info couldn't be written to the cache.");
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Uri authCodeURI = getIntent().getData();

        Log.i("CODE", authCodeURI.toString());

        String code = authCodeURI.getQueryParameter("code");
        String error = authCodeURI.getQueryParameter("error");

        try {
            code = authCodeURI.getQueryParameter("code");
            error = authCodeURI.getQueryParameter("error");
        } catch (NullPointerException e) { Log.i(TAG, "Problem extracting data from Intent's Uri."); }

        if (TextUtils.isEmpty(error) && !TextUtils.isEmpty(code)) {
            runAuthentication(code, true);

            if (!getIntent().getBooleanExtra("instrumentationTest", false)) {
                Firestore.get().getAndSetUserData(
                        Player.get().getSciper(),
                        Player.get().getFirstName(),
                        Player.get().getLastName());
            }

            Firestore.get().getAndSetUserData(
                    Player.get().getSciper(),
                    Player.get().getFirstName(),
                    Player.get().getLastName());

            Utils.waitAndTag(Utils.TIME_TO_WAIT_FOR_LOGIN, TAG);

            boolean dataHasBeenReceived = Player.get().getFirstName() != Utils.INITIAL_FIRSTNAME
                    || Player.get().getLastName() != Utils.INITIAL_LASTNAME
                    || Player.get().getSciper() != Utils.INITIAL_SCIPER;
            if(dataHasBeenReceived) putPlayerDataToCache();

        } else {
            reportAuthError();
            Log.e("AUTH ERROR", error);
        }


        Intent returnToLoginActivity = new Intent(AuthenticationActivity.this, LoginActivity.class);
        returnToLoginActivity.putExtra(
            getString(R.string.post_login_message_key),
            getString(R.string.login_failed_value)
        );
    }
}
