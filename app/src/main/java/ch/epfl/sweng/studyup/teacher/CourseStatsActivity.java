package ch.epfl.sweng.studyup.teacher;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.player.UserData;
import ch.epfl.sweng.studyup.questions.DisplayQuestionActivity;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants.Course;
import ch.epfl.sweng.studyup.utils.adapters.ListCourseAdapter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationTeacher;

import static ch.epfl.sweng.studyup.utils.Constants.COURSE_STAT_INDEX;
import static ch.epfl.sweng.studyup.utils.Constants.FB_ANSWERED_QUESTIONS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSE;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES;
import static ch.epfl.sweng.studyup.utils.Constants.FB_COURSES_ENROLLED;
import static ch.epfl.sweng.studyup.utils.Constants.FB_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_LASTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTIONS;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_ANSWER;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_LANG;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_TITLE;
import static ch.epfl.sweng.studyup.utils.Constants.FB_QUESTION_TRUEFALSE;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SCIPER;
import static ch.epfl.sweng.studyup.utils.Constants.FB_USERS;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_FIRSTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_LASTNAME;
import static ch.epfl.sweng.studyup.utils.Constants.INITIAL_SCIPER;
import static ch.epfl.sweng.studyup.utils.Utils.getCourseListFromStringList;
import static ch.epfl.sweng.studyup.utils.Utils.getOrDefault;
import static ch.epfl.sweng.studyup.utils.Utils.setupToolbar;

public class CourseStatsActivity extends NavigationTeacher {

    private ListCourseAdapter listCourseAdapter;
    private static List<UserData> allUsers = new ArrayList<>();
    private static List<Question> allQuestions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_stats);
        setupToolbar(this);
        navigationSwitcher(CourseStatsActivity.this, CourseStatsActivity.class, COURSE_STAT_INDEX);

        setupListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllQuestions(this);
        loadUsersForStats(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setupListView();
    }

    //retrieve users from firebase
    public static void setUsers(List<UserData> userList) {allUsers = userList; }
    //retrieve questions from firebase
    public static void setQuestions(List<Question> qList) { allQuestions = qList;}

    public List<UserData> getAllUsers(){ return allUsers; }

    public List<Question> getAllQuestions(){ return allQuestions; }



    private void setupListView() {
        ListView listView = findViewById(R.id.listViewCourses);

        ArrayList<Course> playerCourses = new ArrayList<>(Player.get().getCoursesTeached());

        listCourseAdapter = new ListCourseAdapter(this, playerCourses, R.layout.course_item_model, true);
        listView.setAdapter(listCourseAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Course c = (Course) listCourseAdapter.getItem(position);
                startActivity(new Intent(parent.getContext(), DisplayCourseStatsActivity.class).putExtra(DisplayQuestionActivity.class.getName(), c.name()));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    public int setColor(int rate, int nb_answer) {
        if (nb_answer==0) return Color.parseColor("#6C6F6F");
        else if(rate>70) return Color.parseColor("#63B97F");
        else if(rate<40) return Color.parseColor("#CB0814");
        else return Color.parseColor("#EC804D");
    }

    @SuppressWarnings("unchecked")
    public void loadUsersForStats(final Activity act) {
        final List<UserData> userList = new ArrayList<>();

        Firestore.get().getDb().collection(FB_USERS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        Map<String, Object> remotePlayerData = document.getData();

                        UserData user = new UserData(INITIAL_SCIPER,
                                INITIAL_FIRSTNAME,
                                INITIAL_LASTNAME,
                                new HashMap<String, List<String>>(),
                                new ArrayList<Course>());
                        user.setSciperNum(getOrDefault(remotePlayerData, FB_SCIPER, INITIAL_SCIPER).toString());
                        user.setFirstName(getOrDefault(remotePlayerData, FB_FIRSTNAME, INITIAL_FIRSTNAME).toString());
                        user.setLastName(getOrDefault(remotePlayerData, FB_LASTNAME, INITIAL_LASTNAME).toString());
                        user.setAnsweredQuestions((HashMap<String, List<String>>) getOrDefault(remotePlayerData, FB_ANSWERED_QUESTIONS, new HashMap<>()));
                        user.setCourses(getCourseListFromStringList((List<String>) getOrDefault(remotePlayerData, FB_COURSES_ENROLLED, new ArrayList<Course>())));

                        userList.add(user);
                    }

                    if (act instanceof CourseStatsActivity) {
                        CourseStatsActivity.setUsers(userList);
                    }
                } else {
                    Log.e(this.getClass().getSimpleName(), "Error getting documents for courses: ", task.getException());
                }
            }
        });

    }


    /**
     *
     * Load all questions in order to give statistics for each course, students, questions
     *
     * @param act activity in which this function can be used : CourseStatsActivity
     * @throws NullPointerException  If the data received from the server is not of a valid format
     */
    public void loadAllQuestions(final Activity act) throws NullPointerException {

        final List<Question> questionList = new ArrayList<>();

        Firestore.get().getDb().collection(FB_QUESTIONS).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Map<String, Object> remoteQuestionData = document.getData();
                        String questionId = document.getId();
                        String questionTitle = (String) remoteQuestionData.get(FB_QUESTION_TITLE);
                        Boolean questionTrueFalse = (Boolean) remoteQuestionData.get(FB_QUESTION_TRUEFALSE);
                        int questionAnswer = Integer.parseInt((remoteQuestionData.get(FB_QUESTION_ANSWER)).toString());
                        String questionCourseName = remoteQuestionData.get(FB_COURSE).toString();
                        String langQuestion = remoteQuestionData.get(FB_QUESTION_LANG).toString();

                        Question question = new Question(questionId, questionTitle, questionTrueFalse, questionAnswer, questionCourseName, langQuestion);


                        questionList.add(question);
                    }
                    if (act instanceof CourseStatsActivity) {
                        CourseStatsActivity.setQuestions(questionList);
                    }

                } else Log.e(this.getClass().getSimpleName(), "Error getting documents for courses: ", task.getException());
            }
        });
    }
}
