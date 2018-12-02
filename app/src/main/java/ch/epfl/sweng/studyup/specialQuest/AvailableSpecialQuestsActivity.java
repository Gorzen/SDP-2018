package ch.epfl.sweng.studyup.specialQuest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import ch.epfl.sweng.studyup.R;

public class AvailableSpecialQuestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_available_special_quests);
    }

    public void onBackButtonAvailableSpecialQuests(View v) {
        finish();
    }
}
