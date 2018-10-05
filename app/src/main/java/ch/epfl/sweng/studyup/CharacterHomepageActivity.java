package ch.epfl.sweng.studyup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;


public class CharacterHomepageActivity extends AppCompatActivity {
    CircularProgressIndicator levelProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_homepage);
        levelProgress = findViewById(R.id.level_progress);
        levelProgress.setProgress(Player.get().getLevelProgress(), 1);
        levelProgress.setStartAngle(270);
    }

    public void addExpPlayer(View view) {
        Player.get().addExperience(Player.XP_STEP);
        levelProgress.setCurrentProgress(Player.get().getLevelProgress());
    }
}
