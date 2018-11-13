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
import java.util.ArrayList;
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
import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;
import static ch.epfl.sweng.studyup.utils.DataContainers.*;


public class AuthenticationActivity extends AppCompatActivity {

    private final String TAG = AuthenticationActivity.class.getSimpleName();

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

        String token = MOCK_ENABLED ? MOCK_TOKEN : Authenticator.getToken(code);

        PlayerDataContainer playerData = Authenticator.getPlayerData(token);

        Player.get().initializePlayerData(playerData.sciperNum, playerData.firstName, playerData.lastname);

        if (!MOCK_ENABLED) {
            Firestore.get().syncPlayerData();
        }
    }

    public void cachePlayerData() {

        Player currPlayer = Player.get();
        FileCacher<List<String>> loginPersistenceCache = new FileCacher<>(this, PERSIST_LOGIN_FILENAME);

        List<String> playerDataCache = new ArrayList<>();

        playerDataCache.add(0, currPlayer.getSciperNum());
        playerDataCache.add(1, currPlayer.getFirstName());
        playerDataCache.add(2, currPlayer.getLastName());
        playerDataCache.add(3, currPlayer.getRole().name());

        try {
            loginPersistenceCache.writeCache(playerDataCache);
        } catch (IOException e) {
            Log.d(TAG, "Unable to cache player data.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            runAuthentication();
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            startActivity(new Intent(AuthenticationActivity.this, LoginActivity.class));
        }

        Utils.waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);

        cachePlayerData();
        Class homeActivity = Player.get().getRole().equals(Role.student) ?
                MainActivity.class : AddQuestionActivity.class;

        startActivity(new Intent(this, homeActivity));
    }
}
