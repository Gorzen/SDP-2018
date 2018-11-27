package ch.epfl.sweng.studyup.teacher;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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

public class CourseStatsActivity extends NavigationTeacher {

    private ListCourseAdapter listCourseAdapter;
    private static List<UserData> allUsers = new ArrayList<>();
    private static List<Question> allQuestions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_stats);
        navigationSwitcher(CourseStatsActivity.this, CourseStatsActivity.class, COURSE_STAT_INDEX);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        setupListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Firestore.get().loadQuestionsForStats(this);
        Firestore.get().loadUsersForStats(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setupListView();
    }

    //retrieve users from firebase
    public static void setUsers(List<UserData> userList) {allUsers = userList;
    }
    //retrieve questions from firebase
    public static void setQuestions(List<Question> qList) { allQuestions = qList;}

    public List<UserData> getAllUsers(){ return allUsers; }

    public List<Question> getAllQuestions(){ return allQuestions; }



    private void setupListView() {
        ListView listView = findViewById(R.id.listViewCourses);

        ArrayList<Course> playerCourses = new ArrayList<>(Player.get().getCoursesTeached());

        listCourseAdapter = new ListCourseAdapter(this, playerCourses);
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

}
