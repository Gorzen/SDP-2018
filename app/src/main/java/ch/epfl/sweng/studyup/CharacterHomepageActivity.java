package ch.epfl.sweng.studyup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import az.plainpie.PieView;
import az.plainpie.animation.PieAngleAnimation;

public class CharacterHomepageActivity extends AppCompatActivity {
    private Player player;
    public static final int XP_STEP = 10;
    private PieView pieView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_homepage);
        pieView = (PieView) findViewById(R.id.pieView);
        pieViewAnimation();
        player = Player.get();
    }

    /**
     * Start the animation of the pieView which display the percentage of progression
     */
    private void pieViewAnimation() {
        PieAngleAnimation animation = new PieAngleAnimation(pieView);
        animation.setDuration(5000); //This is the duration of the animation in millis
        pieView.startAnimation(animation);
    }

    public void addExpPlayer(View view) {
        player.addExperience(XP_STEP);
        pieView.setPercentage(player.getExperience());
        pieViewAnimation();
    }
}
