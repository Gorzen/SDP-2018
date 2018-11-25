package ch.epfl.sweng.studyup.firebase;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import ch.epfl.sweng.studyup.WeekViewEvent;
import ch.epfl.sweng.studyup.map.Room;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.questions.QuestionParser;
import ch.epfl.sweng.studyup.utils.Rooms;

import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSE;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES_ENROLLED;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES_TEACHED;
import static ch.epfl.sweng.studyup.utils.Constants.FB_CURRENCY;
import static ch.epfl.sweng.studyup.utils.Constants.FB_EVENTS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_ITEMS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_LASTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTIONS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_ANSWER;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_AUTHOR;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_TITLE;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_TRUEFALSE;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SCIPER;
import static ch.epfl.sweng.studyup.utils.Constants.FB_USERNAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_USERS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_XP;
import static ch.epfl.sweng.studyup.utils.Constants.MAX_SCIPER;
import static ch.epfl.sweng.studyup.utils.Constants.MIN_SCIPER;
import static ch.epfl.sweng.studyup.utils.Constants.Role;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.DB_STATIC_INFO;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_UUID;
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

    /**
     * Function used to synchronize the data of the player, either from local to the remote
     * or from the remote to the local depending if it is a new user or not.
     *
     * @throws NullPointerException If the data received from the server is not of a valid format
     * @throws IllegalArgumentException If the sciper of the player is not valid
     */
    public void syncPlayerData() throws NullPointerException, IllegalArgumentException {
        final Player currPlayer = Player.get();
        final int intSciper = Integer.parseInt(currPlayer.getSciperNum());

        if (intSciper < MIN_SCIPER || intSciper > MAX_SCIPER) {
            throw new IllegalArgumentException("The Sciper number should be a six digit number.");
        }

        db.collection(FB_USERS).document(currPlayer.getSciperNum())
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
                            updateRemotePlayerDataFromLocal();
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
        localPlayerData.put(FB_USERNAME, currPlayer.getUserName());
        localPlayerData.put(FB_XP, currPlayer.getExperience());
        localPlayerData.put(FB_CURRENCY, currPlayer.getCurrency());
        localPlayerData.put(FB_LEVEL, currPlayer.getLevel());
        localPlayerData.put(FB_ITEMS, currPlayer.getItemNames());
        localPlayerData.put(FB_COURSES_ENROLLED, getStringListFromCourseList(currPlayer.getCoursesEnrolled()));
        localPlayerData.put(FB_COURSES_TEACHED, getStringListFromCourseList(currPlayer.getCoursesTeached()));

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
        Map<String, Object> questionData = new HashMap<>();
        questionData.put(FB_QUESTION_TRUEFALSE, question.isTrueFalse());
        questionData.put(FB_QUESTION_ANSWER, question.getAnswer());
        questionData.put(FB_QUESTION_TITLE, question.getTitle());
        questionData.put(FB_COURSE, question.getCourseName());
        questionData.put(FB_QUESTION_AUTHOR, Player.get().getSciperNum());

        db.collection(FB_QUESTIONS).document(question.getQuestionId()).set(questionData);
    }

    /**
     * Load all questions that have not been created by the current player if role is student.
     * Load all questions that have been created by the current player if the role is teacher.
     * In addition, only questions that correspond to the current player's courses should be loaded.
     *
     * @param context The context used to save the questions locally
     * @throws NullPointerException If the data received from the server is not of a valid format
     */
    public void loadQuestions(final Context context) throws NullPointerException {

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

                    // Questions without associated courses (created before this feature), will appear for all players
                    boolean questionCourseMatchesPlayer = true;
                    if (questionData.get(FB_COURSE) != null) {
                        // If question is associated with a course, only load question if the user enrolled in that course.
                        String questionCourseName = questionData.get(FB_COURSE).toString();
                        questionCourseMatchesPlayer =
                                Player.get().getCoursesEnrolled().contains(Course.valueOf(questionCourseName));
                    }

                    boolean isValidQuestion = questionCourseMatchesPlayer &&
                            ((currPlayerIsAuthor && currPlayer.getRole() == Role.teacher) ||
                             (!currPlayerIsAuthor && currPlayer.getRole() == Role.student));

                    if(isValidQuestion) {

                        String questionTitle = (String) questionData.get(FB_QUESTION_TITLE);
                        Boolean questionTrueFalse = (Boolean) questionData.get(FB_QUESTION_TRUEFALSE);
                        int questionAnswer = Integer.parseInt((questionData.get(FB_QUESTION_ANSWER)).toString());
                        String questionCourseName = questionData.get(FB_COURSE) == null ? Course.SWENG.name() : questionData.get(FB_COURSE).toString();


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

    /**
     * Method that get the schedule of the current player, that he/she be teacher or student, and
     * will update the layout accordingly using the updateSchedule method in the activity given as
     * parameter and/or update the schedule of the player
     *
     * @param act  The activity displaying the layout (if it is a schedule activity, ignored otherwise)
     * @param role The role, which the caller can choose
     * @throws NullPointerException     If the format is incorrect on the database
     */
    public void getCoursesSchedule(final Activity act, final Role role) throws NullPointerException {
        final Player p = Player.get();
        final CollectionReference coursesRef = db.collection(FB_COURSES);
        final List<WeekViewEvent> schedule = new ArrayList<>();
        final boolean isTeacher = p.getRole() == Role.teacher;

        final AtomicInteger courseCounter = new AtomicInteger(0);
        final List<Course> courses = isTeacher ? Player.get().getCoursesTeached() : Player.get().getCoursesEnrolled();
        // Iteration over all events of all needed courses
        for(final Course c : courses) {
            coursesRef.document(c.name()).collection(FB_EVENTS).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            // Adding periods to the course
                            for (QueryDocumentSnapshot q : queryDocumentSnapshots) {
                                schedule.add(queryDocumentSnapshotToWeekView(q));
                            }

                            courseCounter.incrementAndGet();
                        }
                    });
        }

        while(courseCounter.get() < courses.size()) {
            waitAndTag(100, TAG);
        }

        onScheduleCompleted(schedule);
    }

    private WeekViewEvent queryDocumentSnapshotToWeekView(QueryDocumentSnapshot q) {
        long id = Long.parseLong(q.get("id").toString());
        Map<String, Object> startMap = (Map<String, Object>) q.get("startTime");
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis((Long) startMap.get("timeInMillis"));
        Map<String, Object> endMap = (Map<String, Object>) q.get("startTime");
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis((Long) endMap.get("timeInMillis"));
        String name = q.get("name").toString();
        String location = q.get("location").toString();


        return new WeekViewEvent(id, name, location, start, end);
    }

    private void onScheduleCompleted(final List<WeekViewEvent> schedule) {
            /*
                To put once implemented

            if(act instanceof ScheduleActivity) {
            (    (ScheduleActivity) act).updateSchedule(schedule);
            }
            */

            if(Player.get().getRole() == Role.student) {
                Player.get().setScheduleStudent(schedule);
            }
    }

    /**
     * Used when a teacher select her/his courses, replace the old course if someone else was
     * teaching it or don't do anything if the teacher where already teaching this course. The
     * teacher is the current player.
     *
     * @param c The course
     */
    public void setCourseTeacher(final Course c) {

        db.collection(FB_COURSES).document(c.name()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot doc = task.getResult();

                            if(!doc.exists() || Player.get().getSciperNum().equals(doc.getData().get(FB_SCIPER).toString())) {
                                changeCourseTeacher(c);
                            }

                        } else {
                            Log.w(TAG, "The schedule fail to load or no course are present.");
                        }
                    }
                });
    }

    private void changeCourseTeacher(final Course c) {
        // Setting new teacher
        Map<String, Object> courseData = new HashMap<>();
        courseData.put(FB_SCIPER, Player.get().getSciperNum());
        db.collection(FB_COURSES).document(c.name()).set(courseData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Teacher successfully updated.");
                    }
                });

        // Deleting previous teacher schedule
        db.collection(FB_COURSES).document(c.name()).collection(FB_EVENTS).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(final QueryDocumentSnapshot q : queryDocumentSnapshots) {
                            q.getReference().delete()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.i(TAG, "Failed during an old teacher period's deletion: \n"+q.toObject(WeekViewEvent.class));
                                        }
                                    });
                        }
                    }
                });
    }

    /**
     * Add periods to the course on the server by removing the old ones with corresponding start
     * time and adding the new ones.
     *
     * @param c The course
     * @param periodsToAdd The times of the periods for the course, containing all needed data
     *                     (Room, startTime, endTime, etc...)
     */
    public void addEventsToCourse(final Course c, final List<WeekViewEvent> periodsToAdd) {
        final CollectionReference eventsOfCourse = db.collection(FB_COURSES).document(c.name()).collection(FB_EVENTS);



        eventsOfCourse.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.i(TAG, "Successfully recovered the course's current periods.");

                        // Deleting periods that are replaced
                        for(QueryDocumentSnapshot q : queryDocumentSnapshots) {
                            WeekViewEvent periodChecked = queryDocumentSnapshotToWeekView(q);

                            for(WeekViewEvent p : periodsToAdd) {
                                if(p.getStartTime() == periodChecked.getStartTime()) {
                                    q.getReference().delete();
                                }
                            }
                        }

                        // Adding new periods
                        for(WeekViewEvent p : periodsToAdd) {
                            eventsOfCourse.add(p);
                        }
                    }
                });
    }

    public void deleteCourse(Course c) {
        changeCourseTeacher(c); // To delete the periods on the server
        db.collection(FB_COURSES).document(c.name()).delete();
    }
}
