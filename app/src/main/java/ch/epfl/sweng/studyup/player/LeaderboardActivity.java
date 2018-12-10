package ch.epfl.sweng.studyup.player;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Callback;
import ch.epfl.sweng.studyup.utils.Constants.Course;
import ch.epfl.sweng.studyup.utils.NonScrollableListView;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.adapters.StudentRankingAdapter;

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAllQuestions(handleQuestionsData);
        loadUsers(handleUsersData);
    }

    public Callback<List> handleQuestionsData = new Callback<List>() {
        public void call(List questionList) {
            allQuestions = questionList;
        }
    };

    public Callback<List> handleUsersData = new Callback<List>() {
        public void call(List userList) {
            allUsers = userList;
            displayRankings();
        }
    };

    public void displayRankings() {
        for (Course course : Player.get().getCoursesEnrolled()) {
            List<UserData> studentsInCourse = getStudentsForCourse(allUsers, course);
            List<String> courseQuestionIds = getQuestionIdsForCourse(allQuestions, course);

            //List<Pair<String, Integer>> studentRankings = getStudentRankingsForCourse(course, studentsInCourse, courseQuestionIds);
            List<Pair<String, Integer>> studentRankings = new ArrayList<>();
            studentRankings.add(new Pair<>("Bob Sheffield", 6));
            studentRankings.add(new Pair<>("Marc Anthony", 3));
            studentRankings.add(new Pair<>("Craig Williams", 3));
            studentRankings.add(new Pair<>("Nick Mayer", 2));
            studentRankings.add(new Pair<>("Richard Rich", 2));
            studentRankings.add(new Pair<>("Thomas More", 1));

            if (studentRankings.size() > 0) {
                displayTitleForCourse(course.name());
                displayRankingForCourse(studentRankings);
            }
        }
    }

    public List<Pair<String, Integer>> getStudentRankingsForCourse(Course course, List<UserData> studentsInCourse, final List<String> courseQuestionIds) {

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
            if (correctAnswers > 0) {
                studentRankings.add(new Pair<>(student.getFirstName() + " " + student.getLastName(), correctAnswers));
            }

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

    public void displayTitleForCourse(String courseName) {

        LinearLayout leaderboardContainer = findViewById(R.id.leaderboard_container);

        TextView courseTitle = new TextView(this);
        courseTitle.setTextSize(24);
        courseTitle.setGravity(Gravity.CENTER);
        courseTitle.setText(courseName);
        leaderboardContainer.addView(courseTitle);

        TextView correctAnswersLabel = new TextView(this);
        correctAnswersLabel.setGravity(Gravity.RIGHT);
        correctAnswersLabel.setPadding(0, 0, 20, 0);
        correctAnswersLabel.setText(R.string.correct_answers_label);
        leaderboardContainer.addView(correctAnswersLabel);
    }

    public void displayRankingForCourse(List<Pair<String, Integer>> studentRankings) {

        LinearLayout leaderboardContainer = findViewById(R.id.leaderboard_container);

        NonScrollableListView rankingListView = new NonScrollableListView(this);
        rankingListView.setPadding(0, 0, 0, 20);

        StudentRankingAdapter rankingAdapter =
                new StudentRankingAdapter(this, R.layout.student_ranking_model, studentRankings);
        rankingListView.setAdapter(rankingAdapter);

        leaderboardContainer.addView(rankingListView);
    }

    public void onBackButtonLeaderboardActivity(View v) { finish(); }
}
