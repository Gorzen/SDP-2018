package ch.epfl.sweng.studyup.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.RefreshContext;

import static ch.epfl.sweng.studyup.utils.Constants.COLOR_SETTINGS_KEYWORD;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_BLUE;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_BROWN;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_GREEN;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_DARK;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_RED;
import static ch.epfl.sweng.studyup.utils.Constants.USER_PREFS;
import static ch.epfl.sweng.studyup.utils.Utils.setupColor;

public class ChooseColorActivity extends RefreshContext {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_color);
    }

    public void setColorRed(View v) {
        setupSettingsColor(SETTINGS_COLOR_RED);
        setupColor(SETTINGS_COLOR_RED);
        backToApp();
        finish();
    }

    public void setColorGreen(View v) {
        setupSettingsColor(SETTINGS_COLOR_GREEN);
        setupColor(SETTINGS_COLOR_GREEN);
        backToApp();
        finish();
    }

    public void setColorBlue(View v) {
        setupSettingsColor(SETTINGS_COLOR_BLUE);
        setupColor(SETTINGS_COLOR_BLUE);
        backToApp();
        finish();
    }

    public void setColorBrown(View v) {
        setupSettingsColor(SETTINGS_COLOR_BROWN);
        setupColor(SETTINGS_COLOR_BROWN);
        backToApp();
        finish();
    }

    public void setColorDark(View v) {
        setupSettingsColor(SETTINGS_COLOR_DARK);
        setupColor(SETTINGS_COLOR_DARK);
        backToApp();
        finish();
    }

    private void backToApp() {
        Class act = Player.get().isTeacher() ?
                QuestsActivityTeacher.class : HomeActivity.class;
        startActivity(new Intent(this, act));
    }

    private void setupSettingsColor(String col) {
        getSharedPreferences(USER_PREFS, MODE_PRIVATE).edit()
                .putString(COLOR_SETTINGS_KEYWORD, col)
                .apply();
    }

    public void backToSettings(View v) {
        finish();
    }
}