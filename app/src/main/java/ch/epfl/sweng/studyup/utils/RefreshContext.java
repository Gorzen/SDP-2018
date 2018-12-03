package ch.epfl.sweng.studyup.utils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ch.epfl.sweng.studyup.R;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.APP_THEME;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

public class RefreshContext extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MOST_RECENT_ACTIVITY = this;
        setTheme(APP_THEME);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MOST_RECENT_ACTIVITY = this;
    }
}
