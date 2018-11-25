package ch.epfl.sweng.studyup.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ch.epfl.sweng.studyup.player.MainActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
import ch.epfl.sweng.studyup.utils.RefreshContext;

public class ChooseColorActivity extends RefreshContext {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_color);
    }

    public void setColorRed(View v) {
        GlobalAccessVariables.APP_THEME = R.style.AppTheme;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void setColorGreen(View v) {
        GlobalAccessVariables.APP_THEME = R.style.AppThemeGreen;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void setColorBlue(View v) {
        GlobalAccessVariables.APP_THEME = R.style.AppThemeBlue;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void setColorBrown(View v) {
        GlobalAccessVariables.APP_THEME = R.style.AppThemeBrown;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void setColorMulti(View v) {
        GlobalAccessVariables.APP_THEME = R.style.AppThemeMulti;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void backToSettings(View v) {
        Intent toSettings = new Intent(ChooseColorActivity.this, SettingsActivity.class);
        startActivity(toSettings);
    }
}