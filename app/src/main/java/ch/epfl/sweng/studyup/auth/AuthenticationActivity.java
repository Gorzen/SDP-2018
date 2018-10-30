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

                    Utils.waitAndTag(250, TAG);
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
