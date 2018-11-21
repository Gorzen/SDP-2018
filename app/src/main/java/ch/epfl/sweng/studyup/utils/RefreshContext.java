package ch.epfl.sweng.studyup.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.APP_THEME;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

public class RefreshContext extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(APP_THEME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MOST_RECENT_ACTIVITY = this;
    }
}
