package ch.epfl.sweng.studyup.auth;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;

import ch.epfl.sweng.studyup.utils.Utils;
import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;
import static ch.epfl.sweng.studyup.utils.DataContainers.*;


public class AuthenticationActivity extends AppCompatActivity {

    private final String TAG = AuthenticationActivity.class.getSimpleName();

    public void reportAuthError() {
        Toast.makeText(AuthenticationActivity.this,
                "Unable to authenticate.",
                Toast.LENGTH_SHORT).show();
    }

    public void runAuthentication() throws Exception {

        Uri authCodeURI = getIntent().getData();

        String code = authCodeURI.getQueryParameter("code");
        String error = authCodeURI.getQueryParameter("error");

        if (!TextUtils.isEmpty(error)) {
            throw new Exception("Error when trying to get code: " + error);
        }
        if (TextUtils.isEmpty(code)) {
            throw new Exception("Unable to retrieve code.");
        }

        String token = isMockEnabled ? "NON-NULL TOKEN" : Authenticator.getToken(code);

        PlayerDataContainer playerData = Authenticator.getPlayerData(token);



                    if (!getIntent().getBooleanExtra("instrumentationTest", false)) {
                        Firestore.get().getAndSetUserData(
                                Player.get().getSciper(),
                                Player.get().getFirstName(),
                                Player.get().getLastName());
                    }

                    Utils.waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);

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

    public void putPlayerDataToCache() {

        Player currPlayer = Player.get();

        FileCacher<String[]> persistLogin = new FileCacher<>(this, PERSIST_LOGIN_FILENAME);
        String[] cachedData = new String[4];

        cachedData[0] = String.valueOf(currPlayer.getSciper());
        cachedData[1] = currPlayer.getFirstName();
        cachedData[2] = currPlayer.getLastName();
        cachedData[3] = currPlayer.getRole().name();

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

        runAuthentication(code);

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
