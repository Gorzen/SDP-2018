package ch.epfl.sweng.studyup.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import java.util.Set;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.RefreshContext;

import static ch.epfl.sweng.studyup.utils.Constants.COLOR_SETTINGS_KEYWORD;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_BLUE;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_ORANGE;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_GREEN;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_DARK;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_RED;
import static ch.epfl.sweng.studyup.utils.Constants.USER_PREFS;
import static ch.epfl.sweng.studyup.utils.Utils.setupColor;

public class ChooseColorActivity extends RefreshContext {

    private boolean green_usable, blue_usable, orange_usable, dark_usable = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_color);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupLocksAndClickable();
    }

    public void setupLocksAndClickable() {
        Set<String> unlockedThemes = Player.get().getUnlockedThemes();

        for (String theme : unlockedThemes) {
            if(theme.equals(SETTINGS_COLOR_GREEN))  {
                removeLock(R.id.lockGreen);
                green_usable = true;
            }
            else if(theme.equals(SETTINGS_COLOR_ORANGE)){
                removeLock(R.id.lockOrange);
                orange_usable = true;
            }
            else if(theme.equals(SETTINGS_COLOR_BLUE)){
                removeLock(R.id.lockBlue);
                blue_usable=true;
            }
            else if(theme.equals(SETTINGS_COLOR_DARK)){
                removeLock(R.id.lockMulti);
                dark_usable=true;
            }
        }
    }

    private void removeLock(int idLock) {
        findViewById(idLock).setVisibility(View.GONE);
    }

    private void setColor(String settingsColorBlue, boolean usable) {
        if (usable) {
            setupSettingsColor(settingsColorBlue);
            setupColor(settingsColorBlue);
            backToApp();
            finish();
        } else
            Toast.makeText(getApplicationContext(), getString(R.string.unlocked_theme), Toast.LENGTH_SHORT).show();
    }

    public void setColorRed(View v) {
        setColor(SETTINGS_COLOR_RED, true);
    }

    public void setColorGreen(View v) {
        setColor(SETTINGS_COLOR_GREEN, green_usable);
    }

    public void setColorBlue(View v) {
        setColor(SETTINGS_COLOR_BLUE, blue_usable);
    }

    public void setColorOrange(View v) {
        setColor(SETTINGS_COLOR_ORANGE, orange_usable);
    }

    public void setColorDark(View v) {
        setColor(SETTINGS_COLOR_DARK, dark_usable);
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