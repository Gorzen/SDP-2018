package ch.epfl.sweng.studyup.auth;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.kosalgeek.android.caching.FileCacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.LoginActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.Utils;

import static ch.epfl.sweng.studyup.utils.Constants.FB_USERS;
import static ch.epfl.sweng.studyup.utils.Constants.MAX_SCIPER;
import static ch.epfl.sweng.studyup.utils.Constants.MIN_SCIPER;
import static ch.epfl.sweng.studyup.utils.Constants.PERSIST_LOGIN_FILENAME;
import static ch.epfl.sweng.studyup.utils.Constants.Role;
import static ch.epfl.sweng.studyup.utils.Constants.TIME_TO_WAIT_FOR_LOGIN;
import static ch.epfl.sweng.studyup.utils.DataContainers.PlayerDataContainer;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.HOME_ACTIVITY;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_TOKEN;


public class AuthenticationActivity extends RefreshContext {

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

        Player.get().setSciperNum(playerData.sciperNum);
        Player.get().setFirstName(playerData.firstName);
        Player.get().setLastName(playerData.lastname);
        if (MOCK_ENABLED) {
            Player.get().setRole(Role.student);
        }

        if (!MOCK_ENABLED) {
            syncPlayerData();
        }
    }

    /**
     * Function used to synchronize the data of the player, either from local to the remote
     * or from the remote to the local depending if it is a new user or not.
     *
     * @throws NullPointerException If the data received from the server is not of a valid format
     * @throws IllegalArgumentException If the sciper of the player is not valid
     */
    public static void syncPlayerData() throws NullPointerException, IllegalArgumentException {
        final Player currPlayer = Player.get();
        final int intSciper = Integer.parseInt(currPlayer.getSciperNum());

        if (intSciper < MIN_SCIPER || intSciper > MAX_SCIPER) {
            throw new IllegalArgumentException("The Sciper number should be a six digit number.");
        }

        Firestore.get().getDb().collection(FB_USERS).document(currPlayer.getSciperNum())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {

                                Map<String, Object> remotePlayerData = document.getData();
                            /*
                            Player is a return user. They have stored remote data.
                            Update local data with remote data.
                             */
                                currPlayer.updateLocalDataFromRemote(remotePlayerData);
                            } else {
                            /*
                                Player is logging in for the first time.
                                Update remote data with initialized local data.
                                 */
                                Firestore.get().updateRemotePlayerDataFromLocal();
                            }
                        }
                    }
                });
    }

    public static void cachePlayerData(Context cnx) {

        Player currPlayer = Player.get();
        FileCacher<List<String>> loginPersistenceCache = new FileCacher<>(cnx, PERSIST_LOGIN_FILENAME);

        List<String> playerDataCache = new ArrayList<>();

        playerDataCache.add(0, currPlayer.getSciperNum());
        playerDataCache.add(1, currPlayer.getFirstName());
        playerDataCache.add(2, currPlayer.getLastName());
        playerDataCache.add(3, currPlayer.getRole().name());

        try {
            loginPersistenceCache.writeCache(playerDataCache);
        } catch (IOException e) {
            Log.d(cnx.getClass().getSimpleName(), "Unable to cache player data.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        try {
            runAuthentication();
        }
        catch (Exception e) {
            Log.e(TAG, e.getMessage());
            startActivity(new Intent(AuthenticationActivity.this, LoginActivity.class));
            return;
        }

        Utils.waitAndTag(TIME_TO_WAIT_FOR_LOGIN, TAG);

        if(!Player.get().isDefault() && !MOCK_ENABLED) {
            cachePlayerData(this);
        }

        startActivity(new Intent(this, HOME_ACTIVITY));
    }

    public void onContinueToMain(View v) {
        if(Player.get().isStudent()) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, QuestsActivityTeacher.class));
        }
    }

}
