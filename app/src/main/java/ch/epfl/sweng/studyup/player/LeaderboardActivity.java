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
import android.widget.ToggleButton;

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

import static ch.epfl.sweng.studyup.utils.Constants.mockStudentRankings;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static ch.epfl.sweng.studyup.utils.StatsUtils.getQuestionIdsForCourse;
import static ch.epfl.sweng.studyup.utils.StatsUtils.getStudentRankingsForCourse;
import static ch.epfl.sweng.studyup.utils.StatsUtils.getStudentsForCourse;
import static ch.epfl.sweng.studyup.utils.StatsUtils.loadAllQuestions;
import static ch.epfl.sweng.studyup.utils.StatsUtils.loadUsers;

public class LeaderboardActivity extends RefreshContext {

    private static List<UserData> allUsers = new ArrayList<>();
    private static List<Question> allQuestions = new ArrayList<>();

    /*
    Used to compare a list of Pair<String, Integer>s.
    These pairs contain a player name along with a selected ranking metric.
    That is the Integer in the pair will either be the number of correct answers,
    or the XP of the player.
     */
    Comparator studentRankComparator = new Comparator<Pair<String, Integer>>() {
        @Override
        public int compare(Pair<String, Integer> studentA, Pair<String, Integer> studentB) {
            return studentA.second.compareTo(studentB.second) > 0 ? -1 : 1;
        }
    };

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

    /*
    In onResume, all questions and users are loaded using methods in StatsUtils.
    The activity renders the leaderboard from this data. These callbacks are triggered when that data is available.
    After question data is loaded, then user data is loaded. Then the leaderboard is contructed from that data.
     */
    public Callback<List> handleQuestionsData = new Callback<List>() {
        public void call(List questionList) {
            allQuestions = questionList;
        }
    };
    public Callback<List> handleUsersData = new Callback<List>() {
        public void call(List userList) {

            allUsers = userList;

            handleLeadboardModeVisibility(null);
            displayRankingsByXP();
            displayRankingsByQuestionsAnswered();
        }
    };

    /*
    This activity is controlled by a ToggleButton.
    When it is changed, one of two "container" elements are visible.
    One container contains the ranking by correct answers, the other by XP.
     */
    public void handleLeadboardModeVisibility(View view) {
        ToggleButton rankModeToggle = findViewById(R.id.toggle_rank_mode);
        LinearLayout leaderboardByQuestionsAnsweredContainer = findViewById(R.id.leaderboard_by_correct_answers_container);
        LinearLayout leaderboardByXPContainer = findViewById(R.id.leaderboard_by_xp_container);

        if (rankModeToggle.isChecked()) {
            // Toggle has been set to "By XP"
            leaderboardByQuestionsAnsweredContainer.setVisibility(View.VISIBLE);
            leaderboardByXPContainer.setVisibility(View.INVISIBLE);
        }
        else {
            // Toggle has been set to "By Correct Answers"
            leaderboardByQuestionsAnsweredContainer.setVisibility(View.INVISIBLE);
            leaderboardByXPContainer.setVisibility(View.VISIBLE);
        }

        findViewById(R.id.leaderboard_spinner).setVisibility(View.GONE);
    }

    /*
    Generates a single rank list from all players of the app, based on their XP.
     */
    public void displayRankingsByXP() {

        // Compile a list of pairs of student names with their XP
        List<Pair<String, Integer>> studentRankings = new ArrayList<>();
        for (UserData studentData : allUsers) {
            studentRankings.add(new Pair<>(studentData.getFirstName() + " " + studentData.getLastName(), studentData.getXP()));
        }

        if (MOCK_ENABLED) {
            studentRankings = mockStudentRankings;
        }
        displayRankingFromList(studentRankings, findViewById(R.id.leaderboard_by_xp_container), R.string.xp_label);
    }

    /*
    Generates a rank list for each course a player is enrolled in.
     */
    public void displayRankingsByQuestionsAnswered() {
        for (Course course : Player.get().getCoursesEnrolled()) {
            List<UserData> studentsInCourse = getStudentsForCourse(allUsers, course);
            List<String> courseQuestionIds = getQuestionIdsForCourse(allQuestions, course);

            List<Pair<String, Integer>> studentRankings = getStudentRankingsForCourse(studentsInCourse, courseQuestionIds);
            if (MOCK_ENABLED) {
                studentRankings = mockStudentRankings;
            }

            if (studentRankings.size() > 0) {
                displayTitleForCourse(course.name());
                displayRankingFromList(
                        studentRankings,
                        findViewById(R.id.leaderboard_by_correct_answers_container),
                        R.string.correct_answers_label);
            }
        }
    }


    /*
    When viewing leaderboard for correct question answers, display title for a given course.
     */
    public void displayTitleForCourse(String courseName) {

        LinearLayout leaderboardContainer = findViewById(R.id.leaderboard_by_correct_answers_container);

        TextView courseTitle = new TextView(this);
        courseTitle.setTextSize(24);
        courseTitle.setGravity(Gravity.CENTER);
        courseTitle.setText(courseName);
        leaderboardContainer.addView(courseTitle);
    }

    /*
    Used for generating rank list for leaderboard by XP or by correct question answers.
    Called once for each course enrolled. Called a total of once when ranking by XP.
     */
    public void displayRankingFromList(List<Pair<String, Integer>> studentRankings, View container, int metricLabel) {

        LinearLayout leaderboardContainer = (LinearLayout) container;

        TextView correctAnswersLabel = new TextView(this);
        correctAnswersLabel.setGravity(Gravity.RIGHT);
        correctAnswersLabel.setPadding(0, 0, 30, 0);
        correctAnswersLabel.setTextSize(18);
        correctAnswersLabel.setText(metricLabel);
        leaderboardContainer.addView(correctAnswersLabel);

        NonScrollableListView rankingListView = new NonScrollableListView(this);
        rankingListView.setPadding(0, 0, 0, 20);

        Collections.sort(studentRankings, studentRankComparator);

        StudentRankingAdapter rankingAdapter =
                new StudentRankingAdapter(this, R.layout.student_ranking_model, studentRankings);
        rankingListView.setAdapter(rankingAdapter);

        leaderboardContainer.addView(rankingListView);
    }

    public void onBackButtonLeaderboardActivity(View v) { finish(); }
}
