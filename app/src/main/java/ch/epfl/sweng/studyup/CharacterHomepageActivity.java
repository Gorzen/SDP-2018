package ch.epfl.sweng.studyup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

import static java.sql.DriverManager.println; //DEBUG


public class CharacterHomepageActivity extends AppCompatActivity {
    CircularProgressIndicator levelProgress;

    //Texte that will be displayed in the levelProgress layout
    private static final CircularProgressIndicator.ProgressTextAdapter LEVEL_PROGRESS_TEXT = new CircularProgressIndicator.ProgressTextAdapter() {
        @Override
        public String formatText(double progress) {
            return (progress*100+"% of level ").concat(String.valueOf(Player.get().getLevel()+1));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_homepage);
        levelProgress = findViewById(R.id.level_progress);
        levelProgress.setProgress(Player.get().getLevelProgress(), 1);
        levelProgress.setStartAngle(270);
        levelProgress.setProgressTextAdapter(null);
    }

    public void addExpPlayer(View view) {
        Player.get().addExperience(Player.XP_STEP);
        levelProgress.setCurrentProgress(Player.get().getLevelProgress());
        levelProgress.setProgressTextAdapter(LEVEL_PROGRESS_TEXT);
    }
}