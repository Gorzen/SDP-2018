package ch.epfl.sweng.studyup.player;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.api.Distribution;

import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.Constants.Course;
import ch.epfl.sweng.studyup.utils.RefreshContext;

public class LeaderboardActivity extends RefreshContext {

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

    public void displayRankingForCourse(Course course) {

    }

    public void onBackButtonLeaderboardActivity(View v) { finish(); }
}
