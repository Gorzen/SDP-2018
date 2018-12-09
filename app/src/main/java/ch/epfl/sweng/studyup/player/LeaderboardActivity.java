package ch.epfl.sweng.studyup.player;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.Distribution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Callback;
import ch.epfl.sweng.studyup.utils.Constants.Course;
import ch.epfl.sweng.studyup.utils.RefreshContext;

import static ch.epfl.sweng.studyup.utils.StatsUtils.getQuestionIdsForCourse;
import static ch.epfl.sweng.studyup.utils.StatsUtils.getStudentsForCourse;
import static ch.epfl.sweng.studyup.utils.StatsUtils.loadAllQuestions;
import static ch.epfl.sweng.studyup.utils.StatsUtils.loadUsers;

public class LeaderboardActivity extends RefreshContext {

    private static List<UserData> allUsers = new ArrayList<>();
    private static List<Question> allQuestions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        LinearLayout leaderboardContainer = findViewById(R.id.leaderboard_container);
        for (Course course : Player.get().getCoursesEnrolled()) {
            TextView courseTitle = new TextView(this);
            courseTitle.setTextSize(24);
            courseTitle.setText(course.name());
            leaderboardContainer.addView(courseTitle);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllQuestions(handleQuestionsData);
        loadUsers(handleUsersData);
    }

    public static Callback<List> handleQuestionsData = new Callback<List>() {
        public void call(List questionList) {
            allQuestions = questionList;
        }
    };

    public static Callback<List> handleUsersData = new Callback<List>() {
        public void call(List userList) {
            allUsers = userList;
            displayRankings();
        }
    };

    public static void displayRankings() {
        for (Course course : Player.get().getCoursesEnrolled()) {
            List<UserData> studentsInCourse = getStudentsForCourse(allUsers, course);
            List<String> courseQuestionIds = getQuestionIdsForCourse(allQuestions, course);

            List<Pair<String, Integer>> studentRankings = getStudentRankingsForCourse(course, studentsInCourse, courseQuestionIds);
            displayRankingForCourse(studentRankings);
        }
    }

    public static List<Pair<String, Integer>> getStudentRankingsForCourse(Course course, List<UserData> studentsInCourse, final List<String> courseQuestionIds) {

        List<Pair<String, Integer>> studentRankings = new ArrayList<>();

        for (UserData student : studentsInCourse) {
            int correctAnswers = 0;
            HashMap<String, List<String>> studentAnsweredQuestionData = student.getAnsweredQuestions();
            for (String questionId : studentAnsweredQuestionData.keySet()) {
                if (courseQuestionIds.contains(questionId) &&
                        Boolean.valueOf(studentAnsweredQuestionData.get(questionId).get(0)) == true) {
                    correctAnswers ++;
                }
            }
            studentRankings.add(new Pair<>(student.getFirstName() + " " + student.getLastName(), correctAnswers));
        }

        Collections.sort(studentRankings, new Comparator<Pair<String, Integer>>() {
                @Override
                public int compare(Pair<String, Integer> studentA, Pair<String, Integer> studentB) {
                    return studentA.second >= studentB.second ? 0 : 1;
                }
            }
        );

        return studentRankings;
    }

    public static void displayRankingForCourse(List<Pair<String, Integer>> studentRankings) {
        // Do stuff with student rankings
    }

    public void onBackButtonLeaderboardActivity(View v) { finish(); }
}
