package ch.epfl.sweng.studyup.utils;

import android.support.v7.app.AppCompatActivity;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

public abstract class RefreshContext extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        MOST_RECENT_ACTIVITY = this;
    }
}
