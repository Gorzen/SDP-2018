package ch.epfl.sweng.studyup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class CharacterHomepageActivity extends AppCompatActivity {
    public static final int XP_STEP = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_homepage);
    }

    public void addExpPlayer(View view) {
        Player.get().addExperience(XP_STEP);
    }
}
