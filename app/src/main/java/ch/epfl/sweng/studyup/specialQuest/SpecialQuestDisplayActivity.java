package ch.epfl.sweng.studyup.specialQuest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import ch.epfl.sweng.studyup.R;

import static ch.epfl.sweng.studyup.utils.Constants.SPECIAL_QUEST_KEY;

/*
Basic implementation of an activity for displaying a special quest.
For now it simply displays the title, description, and progress.
Progress, of course, is initially zero.
 */
public class SpecialQuestDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_quest_display);

        SpecialQuest specialQuest = (SpecialQuest) getIntent().getSerializableExtra(SPECIAL_QUEST_KEY);

        TextView titleView = findViewById(R.id.specialQuestTitle);
        titleView.setText(specialQuest.getTitle());

        TextView descriptionView = findViewById(R.id.specialQuestDescription);
        descriptionView.setText(specialQuest.getDescription());

        CircularProgressIndicator progressBarView = findViewById(R.id.specialQuestProgress);
        progressBarView.setProgress(specialQuest.getProgress(), 1);
    }
}
