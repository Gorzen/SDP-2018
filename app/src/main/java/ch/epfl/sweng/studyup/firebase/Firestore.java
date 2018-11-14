package ch.epfl.sweng.studyup.firebase;

import android.content.Context;
import android.net.Uri;
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

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;

import static ch.epfl.sweng.studyup.utils.Utils.*;
import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;

/**
 * Firestore
 *
 * Our own Firebase Cloud Firestore API.
 */
public class Firestore {

    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = Firestore.class.getSimpleName();
    private static Firestore instance = null;

    private Firestore() {
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        try { db.setFirestoreSettings(settings); } catch(Exception e) {}
    }

    public static Firestore get() {
        if (instance == null) {
            instance = new Firestore();
        }
        return instance;
    }

    public void syncPlayerData() {

        Player currPlayer = Player.get();
        db.collection(FB_USERS).document(currPlayer.getSciperNum())
            .get()
            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            Map<String, Object> remotePlayerData = document.getData();
                            Player currPlayer = Player.get();

                            if (remotePlayerData.get(FB_FIRSTNAME).equals(currPlayer.getFirstName())
                                && remotePlayerData.get(FB_LASTNAME).equals(currPlayer.getLastName())) {
                                Log.d(TAG, "Calling update local...");
                                /*
                                Player is a return user. They have stored remote data.
                                Update local data with remote data.
                                 */
                                currPlayer.updateLocalDataFromRemote(remotePlayerData);
                            }
                            else {
                                /*
                                Player is logging in for the first time.
                                Update remote data with initialized local data.
                                 */
                                updateRemotePlayerDataFromLocal();
                            }
                        }
                    }
                }
            });
    }

    public void updateRemotePlayerDataFromLocal() {

        Player currPlayer = Player.get();
        Map<String, Object> localPlayerData = new HashMap<>();

        localPlayerData.put(FB_SCIPER, currPlayer.getSciperNum());
        localPlayerData.put(FB_FIRSTNAME, currPlayer.getFirstName());
        localPlayerData.put(FB_LASTNAME, currPlayer.getLastName());
        localPlayerData.put(FB_ROLE, currPlayer.getRole().name());
        localPlayerData.put(FB_USERNAME, currPlayer.getUserName());
        localPlayerData.put(FB_XP, currPlayer.getExperience());
        localPlayerData.put(FB_CURRENCY, currPlayer.getCurrency());
        localPlayerData.put(FB_LEVEL, currPlayer.getLevel());
        localPlayerData.put(FB_ITEMS, currPlayer.getItemNames());

        db.document(FB_USERS + "/" + currPlayer.getSciperNum())
            .set(localPlayerData)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.i(TAG, "Remote player data was updated.");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, "Unable to update remote player data");
                }
            });
    }

    public void deleteUserFromDatabase(String sciperNum) {
        db.collection(FB_USERS).document(sciperNum).delete()
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

    public void addQuestion(final Question question) {

        final Player currPlayer = Player.get();

        db.collection(FB_USERS).document(currPlayer.getSciperNum())
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot document) {
                    if (document.exists()) {

                        String questionId = question.getQuestionId();

                        Map<String, Object> questionData = new HashMap<>();
                        questionData.put(FB_QUESTION_TRUEFALSE, question.isTrueFalse());
                        questionData.put(FB_QUESTION_ANSWER, question.getAnswer());
                        questionData.put(FB_QUESTION_TITLE, question.getTitle());
                        questionData.put(FB_COURSE, question.getCourseName());
                        questionData.put(FB_QUESTION_AUTHOR, currPlayer.getSciperNum());

                        db.collection(FB_QUESTIONS).document(questionId).set(questionData);
                    }
                }
            });
    }

    /**
     * Load all questions that have not been created by the current player if role is student.
     * Load all questions that have been created by the current player if the role is teacher.
     * In addition, only questions that correspond to the current player's courses should be loaded.
     *
     * @param context The context used to save the questions locally
     */
    public void loadQuestions(final Context context) {

        final Player currPlayer = Player.get();

        final List<Question> questionList = new ArrayList<>();

        db.collection(FB_QUESTIONS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    String questionId = document.getId();
                    Map<String, Object> questionData = document.getData();

                    String QuestionAuthorSciperNum = questionData.get(FB_QUESTION_AUTHOR).toString();
                    boolean currPlayerIsAuthor = QuestionAuthorSciperNum.equals(currPlayer.getSciperNum());

                    // TODO: check course
                    //Course questionCourse = Course.valueOf(questionData.get(FB_COURSE).toString());
                    boolean questionInCurrPlayerCourse = true; //currPlayer.getCourses().contains(questionCourse);
                    
                    boolean isValidQuestion = questionInCurrPlayerCourse &&
                            ((currPlayerIsAuthor && currPlayer.getRole().equals(Role.teacher)) ||
                             (!currPlayerIsAuthor && currPlayer.getRole().equals(Role.student)));

                    if(isValidQuestion || questionId.equals(MOCK_UUID)) {

                        String questionTitle = (String) questionData.get(FB_QUESTION_TITLE);
                        Boolean questionTrueFalse = (Boolean) questionData.get(FB_QUESTION_TRUEFALSE);
                        int questionAnswer = Integer.parseInt((questionData.get(FB_QUESTION_ANSWER)).toString());
                        String questionCourseName = Course.SWENG.name(); //questionData.get(FB_COURSE).toString();

                        Question question = new Question(questionId, questionTitle, questionTrueFalse, questionAnswer, questionCourseName);
                        questionList.add(question);
                    }
                }

                QuestionParser.writeQuestions(questionList, context);
                Log.d(TAG, "Question List: " + questionList.toString());
            } else {
                Log.e(TAG, "Error getting documents: ", task.getException());
            }
            }
        });
    }

    /**
     * Method that delete a question and its corresponding image
     *
     * @param questionId the id of the question
     */
    public void deleteQuestion(final String questionId) {
        db.collection(FB_QUESTIONS).document(questionId).delete()
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if(questionId.equals(MOCK_UUID)) {
                            Log.i(TAG, "The question has been deleted from the database.");
                            return;
                        }
                        FileStorage.getProblemImageRef(Uri.parse(questionId + ".png")).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Log.i(TAG, "The question image has been deleted from the database.");
                                }
                            }
                        });
                    } else {
                        Log.e(TAG, "Failed to delete the question's data from the database.");
                    }
                }
            });
    }
}
