package ch.epfl.sweng.studyup.specialQuest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.RefreshContext;

import static ch.epfl.sweng.studyup.utils.Constants.ENGLISH;
import static ch.epfl.sweng.studyup.utils.Constants.SPECIAL_QUEST_INDEX_KEY;

public class SpecialQuestDisplayActivity extends RefreshContext {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special_quest_display);

        loadSpecialQuestData();
    }

    @Override
    public void onResume() {

        super.onResume();
        loadSpecialQuestData();
    }

    public void loadSpecialQuestData() {
        SpecialQuest specialQuest = Player.get().getSpecialQuests().get(
                getIntent().getIntExtra(SPECIAL_QUEST_INDEX_KEY, 0)
        );

        Log.d("SpecialQuestDisplay", "Special quest progress: " + specialQuest.getProgress());

        String displayTitle, displayDesc;
        if (Locale.getDefault().getDisplayLanguage().equals(ENGLISH)) {
            displayTitle = specialQuest.getSpecialQuestType().getEnglishTitle();
            displayDesc = specialQuest.getSpecialQuestType().getEnglishDesc();
        }
        else {
            displayTitle = specialQuest.getSpecialQuestType().getFrenchTitle();
            displayDesc = specialQuest.getSpecialQuestType().getFrenchDesc();
        }

        TextView titleView = findViewById(R.id.specialQuestTitle);
        titleView.setText(displayTitle);

        TextView descriptionView = findViewById(R.id.specialQuestDescription);
        descriptionView.setText(displayDesc);

        CircularProgressIndicator progressBarView = findViewById(R.id.specialQuestProgress);
        progressBarView.setProgressTextAdapter(new CustomProgressTextAdapter());
        progressBarView.setProgress(specialQuest.getProgress() * 100, 100);

        if (specialQuest.getProgress() == 1.0) {
            TextView congratText = findViewById(R.id.specialQuestCongrat);
            congratText.setText(R.string.congrat_text_special_quest);
            TextView rewardItemText = findViewById(R.id.specialQuestReward);
            rewardItemText.setText(specialQuest.getReward().getName());
        }
    }

    public void onBackButtonSpecialQuest(View v) {
        finish();
    }

    public final class CustomProgressTextAdapter implements CircularProgressIndicator.ProgressTextAdapter {

        @Override
        public String formatText(double currentProgress) {
            return String.valueOf((int) currentProgress) + " %";
        }
    }
}
