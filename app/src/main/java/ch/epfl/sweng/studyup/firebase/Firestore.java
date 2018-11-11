package ch.epfl.sweng.studyup.firebase;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;
import ch.epfl.sweng.studyup.utils.Utils;

import static ch.epfl.sweng.studyup.utils.Utils.*;

/**
 * Firestore
 *
 * Our own Firebase Cloud Firestore API.
 */
public class Firestore {
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = Firestore.class.getSimpleName();
    public static Map<String, Object> userData = null;
    private static Firestore instance = null;

    private Firestore() {
        // DB settings
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        try {db.setFirestoreSettings(settings);} catch(Exception e){}
    }

    public static Firestore get() {
        if (instance == null) {
            instance = new Firestore();
        }
        return instance;
    }

    /**
     * This function put the current data of a given user into the dbStaticInfo object (Utils.java).
     *
     * @param sciper Sciper ot the user.
     */
    public static void getData(final int sciper) {
        db.collection(FB_USERS).document(Integer.toString(sciper))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot document) {
                        if (document.exists()) {
                            Utils.dbStaticInfo = document.getData();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {Log.i(TAG, "Error: getData" + sciper); }});
    }

    /**
     * Function used when entering the app. It will get all the player's informations and set
     * the state of the player as it were the last time he/she was connected.
     *
     * @param sciper    The SCIPER numb of the player.
     * @param firstName The first name of the player.
     * @param lastName  The last name of the player.
     * @throws IllegalArgumentException An exception is thrown if the sciper given is incorrect
     */
    public void getAndSetUserData(final int sciper, final String firstName, final String lastName)
            throws IllegalArgumentException {
        if (sciper < MIN_SCIPER || sciper > MAX_SCIPER) {
            throw new IllegalArgumentException("Error: getAndSetUserData, SCIPER number should be" +
                    " a six digits number.");
        }

        db.collection(FB_USERS).document(Integer.toString(sciper))
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                userData = document.getData();
                                if (userData.isEmpty()) {
                                    throw new NullPointerException("The data got from server is " +
                                            "null. The user either shouldn't be present in the " +
                                            "database or should have informations stored.");
                                }

                                if (userData.get(FB_FIRSTNAME) == firstName
                                        && userData.get(FB_LASTNAME) == lastName) {
                                    //User is already logged in
                                    Log.i(TAG, "getAndSetUserData: Success: User was " +
                                            "already logged in:" + sciper);
                                    return;
                                }
                                //New login but user is already in database
                                Log.i(TAG, "getAndSetUserData: Success: New login:" + sciper);
                                Player.get().updatePlayerData(null);
                                if(Player.get().getRole()) {
                                    Firestore.get().setUserData(FB_ROLE, FB_ROLES_T);
                                    putUserData(FB_ROLE, FB_ROLES_T);
                                } else {
                                    Firestore.get().setUserData(FB_ROLE, FB_ROLES_S);
                                    putUserData(FB_ROLE, FB_ROLES_S);
                                }
                            } else {
                                //User is new to the application
                                Log.i(TAG, "getAndSetUserData: Success: New user:" + sciper);
                                Player.get().reset();
                                Player.get().setFirstName(firstName);
                                Player.get().setLastName(lastName);
                                Player.get().setSciper(sciper);
                                savePlayerData();
                            }
                        } else {
                            Log.e(TAG, "getAndSetUserData: Failure: The connection with the server failed, " + task.getException());
                        }
                    }
                });
    }

    /**
     * Method used when mutiple change on the player are made. This will update userData attribute
     * with the current player's state and push it to the server.
     */
    public void savePlayerData() {
        putUserData(FB_XP, Player.get().getExperience());
        putUserData(FB_LEVEL, Player.get().getLevel());
        putUserData(FB_CURRENCY, Player.get().getCurrency());
        putUserData(FB_USERNAME, Player.get().getUserName());

        putUserData(FB_ITEMS, getItemsInt());

        db.document(FB_USERS + "/" + Player.get().getSciper()).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Player infos were saved.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { Log.w(TAG, "Player infos failed to be saved."); }
                });

    }

    /**
     * Method used to update a value for the current player in the database
     * The method suppose that the player is already present in the database.
     *
     * @param key   The key where the value will be put.
     * @param value The value.
     */
    public void setUserData(final String key, final Object value) {
        if (Player.get().getSciper() == INITIAL_SCIPER) {
            return;
        }

        if (!FB_ALL_ENTRIES.contains(key)) { Log.i(TAG, "The key is not valid."); return; }
        putUserData(key, value);

        db.collection(FB_USERS).document(Integer.toString(Player.get().getSciper()))
                .update(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Succes: setUserData(" + Player.get().getSciper() + ").");
                    }
                });
    }

    /**
     * Set the informations of the user corresponding to @sciper to @infos in the database
     *
     * @param sciper The sciper of the player.
     * @param infos  Informations to be put.
     */
    public void setUserInfos(final int sciper, Map<String, Object> infos) {
        for (String key : infos.keySet()) {
            if (!FB_ALL_ENTRIES.contains(key)) { Log.i(TAG, "The key " + key + " is not valid.");return; }
        }

        db.collection(FB_USERS).document(Integer.toString(sciper))
                .update(infos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Succes: setUserInfos(" + sciper + ").");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) { Log.i(TAG, "Error: setUserInfos, " + e.toString()); }
                });
    }

    /**
     * Reset the infos of a given user on the database. If he/she wasn't present, it will create
     * it with the initial values.
     *
     * @param sciper    The SCIPER nmbr of the player.
     * @param firstName The first name of the player.
     * @param lastName  The last name of the player.
     */
    public void resetUserInfos(final int sciper, final String firstName, final String lastName) {
        Map<String, Object> initialInfos = new HashMap<>();
        initialInfos.put(FB_SCIPER, sciper);
        initialInfos.put(FB_FIRSTNAME, firstName);
        initialInfos.put(FB_LASTNAME, lastName);
        initialInfos.put(FB_CURRENCY, INITIAL_CURRENCY);
        initialInfos.put(FB_LEVEL, INITIAL_LEVEL);
        initialInfos.put(FB_XP, INITIAL_XP);

        List<Integer> items = new ArrayList<>();
        initialInfos.put(FB_ITEMS, items);
        initialInfos.put(FB_TOKEN, null);

        setUserInfos(sciper, initialInfos);
    }

    public void deleteUserFromDatabase(int sciper) {
        db.collection(FB_USERS).document(Integer.toString(sciper)).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "The user has been deleted from the database.");
                        } else {
                            Log.w(TAG, "Failed to delete the user from the database.");
                        }
                    }
                });
    }

    public static void addQuestion(Question question) {

        String questionId = question.getQuestionId();

        Map<String, Object> questionData = new HashMap<>();
        questionData.put(FB_QUESTION_TRUEFALSE, question.isTrueFalse());
        questionData.put(FB_QUESTION_ANSWER, question.getAnswer());
        questionData.put(FB_QUESTION_TITLE, question.getTitle());
        questionData.put(FB_QUESTION_AUTHOR, Player.get().getSciper());


        db.collection(FB_QUESTIONS).document(questionId).set(questionData);
    }

    /**
     * Load all the questions on the database the have not been created by the actual player or
     * that have been created by the player, depending on if the player is in teacher or student
     * mode
     *
     * @param context The context used to save the questions locally
     */
    public static void loadQuestions(final Context context) {

        final List<Question> questionList = new ArrayList<>();

        db.collection(FB_QUESTIONS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Map<String, Object> questionData = document.getData();
                        String questionId = document.getId();

                        //If role is teacher, can only view own question, if student only others questions
                        int QuestionAuthor = Integer.parseInt(questionData.get(FB_QUESTION_AUTHOR).toString());
                        boolean getThatQuestion;
                        if(Player.get().getRole()) {
                            getThatQuestion = Player.get().getSciper() == QuestionAuthor;
                        } else {
                            getThatQuestion = Player.get().getSciper() != QuestionAuthor;
                        }

                        if(getThatQuestion) {
                            String title = (String) questionData.get(FB_QUESTION_TITLE);
                            Boolean trueFalse = (Boolean) questionData.get(FB_QUESTION_TRUEFALSE);
                            int answer = Integer.parseInt((questionData.get(FB_QUESTION_ANSWER)).toString());

                            System.out.println("Question: " + title);
                            System.out.println("Answer: " + answer);

                            Question question = new Question(questionId, title, trueFalse, answer);
                            questionList.add(question);
                        }
                    }


                    QuestionParser.writeQuestions(questionList, context);
                    Log.d(TAG, "Question List: " + questionList.toString());
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
