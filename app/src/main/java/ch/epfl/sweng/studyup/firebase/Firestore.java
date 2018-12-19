package ch.epfl.sweng.studyup.firebase;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;
import ch.epfl.sweng.studyup.utils.Callback;

import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.FB_ANSWERED_QUESTIONS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSE;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES_ENROLLED;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES_TEACHED;
import static ch.epfl.sweng.studyup.utils.Constants.FB_CURRENCY;
import static ch.epfl.sweng.studyup.utils.Constants.FB_EVENTS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_ITEMS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_KNOWN_NPCS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_LASTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTIONS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_ANSWER;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_AUTHOR;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_CLICKEDINSTANT;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_DURATION;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_LANG;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_TITLE;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_TRUEFALSE;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SCIPER;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIALQUESTS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_TEACHING_STAFF;
import static ch.epfl.sweng.studyup.utils.Constants.FB_UNLOCKED_THEME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_USERNAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_USERS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_XP;
import static ch.epfl.sweng.studyup.utils.Constants.MOCK_UUIDS;
import static ch.epfl.sweng.studyup.utils.Constants.Role;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.DB_STATIC_INFO;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static ch.epfl.sweng.studyup.utils.Utils.getMapListFromSpecialQuestList;
import static ch.epfl.sweng.studyup.utils.Utils.getStringListFromCourseList;
import static ch.epfl.sweng.studyup.utils.Utils.waitAndTag;

/**
 * Firestore
 *
 * Our own Firebase Cloud Firestore API.
 */
public class Firestore {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
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

    public FirebaseFirestore getDb() {
        return db;
    }

    public void updateRemotePlayerDataFromLocal() {

        Player currPlayer = Player.get();
        Map<String, Object> localPlayerData = new HashMap<>();

        localPlayerData.put(FB_SCIPER, currPlayer.getSciperNum());
        localPlayerData.put(FB_FIRSTNAME, currPlayer.getFirstName());
        localPlayerData.put(FB_LASTNAME, currPlayer.getLastName());
        localPlayerData.put(FB_USERNAME, currPlayer.getUserName());
        localPlayerData.put(FB_XP, currPlayer.getExperience());
        localPlayerData.put(FB_CURRENCY, currPlayer.getCurrency());
        localPlayerData.put(FB_LEVEL, currPlayer.getLevel());
        localPlayerData.put(FB_ITEMS, currPlayer.getItemNames());
        localPlayerData.put(FB_SPECIALQUESTS, getMapListFromSpecialQuestList(currPlayer.getSpecialQuests()));
        localPlayerData.put(FB_COURSES_ENROLLED, getStringListFromCourseList(currPlayer.getCoursesEnrolled(), false));
        localPlayerData.put(FB_COURSES_TEACHED, getStringListFromCourseList(currPlayer.getCoursesTeached(), false));
        localPlayerData.put(FB_ANSWERED_QUESTIONS, currPlayer.getAnsweredQuestion());
        localPlayerData.put(FB_QUESTION_CLICKEDINSTANT, currPlayer.getClickedInstants());
        localPlayerData.put(FB_UNLOCKED_THEME, new ArrayList<>(currPlayer.getUnlockedThemes()));
        localPlayerData.put(FB_KNOWN_NPCS, new ArrayList<>(currPlayer.getKnownNPCs()));

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

    public void addQuestion(final Question question) {
        Map<String, Object> questionData = new HashMap<>();
        questionData.put(FB_QUESTION_TRUEFALSE, question.isTrueFalse());
        questionData.put(FB_QUESTION_ANSWER, question.getAnswer());
        questionData.put(FB_QUESTION_TITLE, question.getTitle());
        questionData.put(FB_COURSE, question.getCourseName());
        questionData.put(FB_QUESTION_AUTHOR, Player.get().getSciperNum());
        questionData.put(FB_QUESTION_LANG, question.getLang());
        questionData.put(FB_QUESTION_DURATION, question.getDuration());

        db.collection(FB_QUESTIONS).document(question.getQuestionId()).set(questionData);
    }

    /**
     * Version without callback of loadQuestions
     */
    public void loadQuestions(final Context context) throws NullPointerException {
        loadQuestions(context, null);
    }

    /**
     * Load all questions that have not been created by the current player if role is student.
     * Load all questions that have been created by the current player if the role is teacher.
     * In addition, only questions that correspond to the current player's courses should be loaded.
     *
     * @param context The context used to save the questions locally
     * @throws NullPointerException If the data received from the server is not of a valid format
     */
    public void loadQuestions(final Context context, final Callback onQuestionsLoaded) throws NullPointerException {

        final Player currPlayer = Player.get();

        final List<Question> questionList = new ArrayList<>();

        db.collection(FB_QUESTIONS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {

                    String questionId = document.getId();
                    Map<String, Object> questionData = document.getData();

                    Question q = extractQuestionData(currPlayer, questionId, questionData);
                    if(q != null) questionList.add(q);
                }

                QuestionParser.writeQuestions(questionList, context);
                if(onQuestionsLoaded != null) {
                    onQuestionsLoaded.call(questionList);
                }
                Log.d(TAG, "Question List: " + questionList.toString());
            } else {
                Log.e(TAG, "Error getting documents: ", task.getException());
            }
            }
        });
    }


    private Question extractQuestionData(Player currPlayer, String id, Map<String, Object> questionData) {
        if(!MOCK_ENABLED && MOCK_UUIDS.contains(id)) return null;
        Course questionCourse = Course.valueOf(questionData.get(FB_COURSE).toString());
        List<Course> playerCourse = currPlayer.isTeacher() ? currPlayer.getCoursesTeached() : currPlayer.getCoursesEnrolled();
        boolean questionCourseMatchesPlayer = playerCourse.contains(questionCourse);

        if (questionCourseMatchesPlayer) {

            String questionTitle = (String) questionData.get(FB_QUESTION_TITLE);
            Boolean questionTrueFalse = (Boolean) questionData.get(FB_QUESTION_TRUEFALSE);
            int questionAnswer = Integer.parseInt((questionData.get(FB_QUESTION_ANSWER)).toString());
            String questionLang = questionData.get(FB_QUESTION_LANG).toString();
            long clickedInstant = getLongValueOrDefault(questionData, FB_QUESTION_CLICKEDINSTANT);
            if (clickedInstant != 0) {
                Player.get().addClickedInstant(id, clickedInstant);
            }
            long duration = getLongValueOrDefault(questionData, FB_QUESTION_DURATION);

            return new Question(id, questionTitle, questionTrueFalse, questionAnswer, questionCourse.name(), questionLang, duration);
        }

        return null;
    }

    private long getLongValueOrDefault(Map<String, Object> questionData, String fbParameter) {
        Object getFromFB = questionData.get(fbParameter);
        if (getFromFB == null) {
            return 0;
        } else {
            return Long.parseLong(getFromFB.toString());
        }
    }

    /**
     * This function put the current data of a given user into the dbStaticInfo object (Utils.java).
     *
     * @param sciper Sciper ot the user.
     */
    public void getData(final int sciper) {
        db.collection(FB_USERS).document(Integer.toString(sciper))
            .get()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot document) {
                    if (document.exists()) {
                        DB_STATIC_INFO = document.getData();
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {Log.i(TAG, "Failed to load data for player with Sciper number: " + sciper); }});
    }

    public void getCoursesSchedule(final Activity act, final Role role) throws NullPointerException {
        FirestoreSchedule.getCoursesSchedule(db, act, role);
    }

    public void addPlayerToTeachingStaff(final Course c, final String sciper) {
        final DocumentReference courseRef = db.collection(FB_COURSES).document(c.name());
        courseRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                DocumentSnapshot doc = task.getResult();

                                List<String> teachers;
                                try {
                                    teachers = (List<String>) doc.getData().get(FB_TEACHING_STAFF);
                                } catch (ClassCastException e) {
                                    Log.d(TAG, "onComplete: The info for the teacher of " + c.name() + " is incorrect.");
                                    return;
                                }

                                if (!teachers.contains(sciper)) {
                                    Map<String, Object> courseData = doc.getData();
                                    teachers.add(sciper);
                                    courseData.put(FB_TEACHING_STAFF, teachers);
                                    courseRef.set(courseData);
                                }
                            } else {
                                Map<String, Object> courseData = new HashMap<>();
                                ArrayList<String> staff = new ArrayList<>(Arrays.asList(sciper));
                                courseData.put(FB_TEACHING_STAFF, staff);
                                courseRef.set(courseData);
                            }

                        } else {
                            Log.w(TAG, "The schedule fail to load or no course are present.");
                        }
                    }
                });
    }

    public void removePlayerFromTeachingStaff(final Course c, final String sciper) {
        final DocumentReference courseRef = db.collection(FB_COURSES).document(c.name());
        courseRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            DocumentSnapshot doc = task.getResult();

                            List<String> teachers;
                            try {
                                teachers = (List<String>) doc.getData().get(FB_TEACHING_STAFF);
                            } catch(ClassCastException e) { Log.d(TAG, "onComplete: The info for the teacher of "+c.name()+" is incorrect."); return; }

                            Map<String, Object> courseData = doc.getData();
                            if(teachers.remove(sciper) && teachers.isEmpty()) {
                                deleteCourseInfos(c);
                            } else {
                                courseData.put(FB_TEACHING_STAFF, teachers);
                                courseRef.set(courseData);
                            }
                        } else {
                            Log.w(TAG, "The schedule fail to load or no course are present.");
                        }
                    }
                });
    }

    private void resetCourseSchedule(final Course c) {
        DocumentReference courseRef = db.collection(FB_COURSES).document(c.name());
        courseRef.collection(FB_EVENTS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(final QueryDocumentSnapshot q : queryDocumentSnapshots) {
                            q.getReference().delete()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i(TAG, "Failed during the deletion of period: \n"+q.toObject(WeekViewEvent.class));
                                        }
                                    });
                        }
                    }
                });
    }
    public void setCourseEvents(final Course c, final List<WeekViewEvent> periodsToAdd) {
        FirestoreSchedule.setCourseEvents(db, c, periodsToAdd);
    }

    public void deleteCourseInfos(Course c) {
        resetCourseSchedule(c); // To delete the periods on the server
        waitAndTag(1000, TAG);
        db.collection(FB_COURSES).document(c.name()).delete();
    }
}
