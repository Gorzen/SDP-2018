package ch.epfl.sweng.studyup;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.HashMap;
import java.util.Map;

import static ch.epfl.sweng.studyup.Player.INITIAL_CURRENCY;
import static ch.epfl.sweng.studyup.Player.INITIAL_LEVEL;
import static ch.epfl.sweng.studyup.Player.INITIAL_XP;
import static ch.epfl.sweng.studyup.Utils.FB_ALL_ENTRIES;
import static ch.epfl.sweng.studyup.Utils.FB_CURRENCY;
import static ch.epfl.sweng.studyup.Utils.FB_FIRSTNAME;
import static ch.epfl.sweng.studyup.Utils.FB_LASTNAME;
import static ch.epfl.sweng.studyup.Utils.FB_LEVEL;
import static ch.epfl.sweng.studyup.Utils.FB_SCIPER;
import static ch.epfl.sweng.studyup.Utils.FB_TOKEN;
import static ch.epfl.sweng.studyup.Utils.FB_USERS;
import static ch.epfl.sweng.studyup.Utils.FB_XP;

public class Firebase {
    private static final String TAG = Firebase.class.getSimpleName();

    private static Firebase instance = null;
    private static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static Map<String, Object> userData;

    private Firebase() {
        // DB settings
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
    }

    public static Firebase get() {
        if(instance == null) {
            instance = new Firebase();
        }

        return instance;
    }

    /**
     * Function used when entering the app. It will get all the player's informations and set
     * the state of the player as it were the last time he/she was connected.
     *
     * @param sciper The sciper of the player
     * @param firstName The first name of the player
     * @param lastName The last name of the player
     *
     * @throws IllegalArgumentException An exception is thrown if the sciper given is incorrect
     */
    public void getAndSetUserData(final int sciper, final String firstName, final String lastName) throws IllegalArgumentException {
        if(sciper < 100000 || sciper > 999999) {
            throw new IllegalArgumentException("Error: getAndSetUserData, SCIPER number should be a six digits number.");
        }

        db.document(FB_USERS+"/"+Integer.toString(sciper))
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userData = document.getData();
                        if(userData.isEmpty()) {
                            throw new NullPointerException("The data got from server is null.");
                        }

                        if(userData.get(FB_FIRSTNAME) == firstName && userData.get(FB_LASTNAME) == lastName) {
                            //User is already logged in
                            Log.i(TAG, "getAndSetUserData: Success: User was already logged in:" + sciper);
                            return;
                        }

                        //New login but user is already in database
                        Log.i(TAG, "getAndSetUserData: Success: New login:" + sciper);
                        Player.get().updatePlayerData();
                    } else {
                        //User is new to the application
                        Log.i(TAG, "getAndSetUserData: Success: New user:" + sciper);
                        Player.get().reset();
                        Player.get().setName(firstName, lastName);
                        Player.get().setSciper(sciper);
                        savePlayerData();
                    }
                } else {
                    Log.e(TAG, "getAndSetUserData: Failure: The connection with the server failed, " + task.getException());
                }
            }
        });
    }

    /*
    public Object getUserData(final String key, final Object value) {
        if(!FB_ALL_ENTRIES.contains(key)) {
            Log.i(TAG, "The key is not valid.");
            return null;
        }
        userData.put(key, value);

        db.document(FB_USERS+"/"+Integer.toString(Player.get().getSciper())+key)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        if(document.exists()) {
                            userData = document.getData();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
    */

    /**
     * Method used when mutiple change on the player are made. This will update userData attribute
     * with the current player's state and push it to the server.
     */
    public void savePlayerData() {
        userData.put(FB_XP, Player.get().getExperience());
        userData.put(FB_LEVEL, Player.get().getLevel());
        userData.put(FB_CURRENCY, Player.get().getCurrency());

        db.document(FB_USERS+"/"+Player.get().getSciper()).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Player infos were saved.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Player infos failed to be saved.");
                    }
                });

    }

    /**
     * Method used to update a value for the current player in the database
     * The method suppose that the player is already present in the database,
     * otherwise the form of its informations will be unpredictable.
     * @param key the key where the value will be put
     * @param value the value
     */
    public void setUserData(final String key, final Object value) {
        if(Player.get().getSciper() == Player.INITIAL_SCIPER){
            return;
        }

        if(!FB_ALL_ENTRIES.contains(key)) {
            Log.i(TAG, "The key is not valid.");
            return;
        }
        userData.put(key, value);

        db.document(FB_USERS+"/"+Integer.toString(Player.get().getSciper()))
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Succes: setUserData(" + Player.get().getSciper() + ").");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Error: setUserData, " + e.toString());
                    }
                });
    }

    /**
     * Set the informations of the user corresponding to @sciper to @infos in the database
     *
     * @param sciper
     * @param infos
     */
    public void setUserInfos(final int sciper, Map<String, Object> infos) {
        for(String key : infos.keySet()) {
            if(!FB_ALL_ENTRIES.contains(key)){
                Log.i(TAG, "The key "+key+" is not valid.");
                return;
            }
        }

        db.collection(FB_USERS).document(Integer.toString(sciper))
                .set(infos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Succes: setUserInfos(" + sciper + ").");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "Error: setUserInfos, " + e.toString());
                    }
                });
    }

    /**
     * Reset the infos of a given user on the database. If he/she wasn't present, it will create
     * it with the initial values.
     *
     * @param sciper
     * @param firstName
     * @param lastName
     */
    public void resetUserInfos(final int sciper, final String firstName, final String lastName) {
        Map<String, Object> initialInfos = new HashMap<>();
        initialInfos.put(FB_SCIPER, sciper);
        initialInfos.put(FB_FIRSTNAME, firstName);
        initialInfos.put(FB_LASTNAME, lastName);
        initialInfos.put(FB_CURRENCY, INITIAL_CURRENCY);
        initialInfos.put(FB_LEVEL, INITIAL_LEVEL);
        initialInfos.put(FB_XP, INITIAL_XP);
        initialInfos.put(FB_TOKEN, null);

        setUserInfos(sciper, initialInfos);
    }
}


