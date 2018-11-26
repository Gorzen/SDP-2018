package ch.epfl.sweng.studyup.settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.Constants;
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
        backToApp();
        finish();
    }

    public void setColorGreen(View v) {
        GlobalAccessVariables.APP_THEME = R.style.AppThemeGreen;
        backToApp();
        finish();
    }

    public void setColorBlue(View v) {
        GlobalAccessVariables.APP_THEME = R.style.AppThemeBlue;
        backToApp();
        finish();
    }

    public void setColorBrown(View v) {
        GlobalAccessVariables.APP_THEME = R.style.AppThemeBrown;
        backToApp();
        finish();
    }

    public void setColorMulti(View v) {
        GlobalAccessVariables.APP_THEME = R.style.AppThemeMulti;
        backToApp();
        finish();
    }

    public void backToApp() {
        Class act = Player.get().getRole() == Constants.Role.teacher ?
                QuestsActivityTeacher.class : HomeActivity.class;
        startActivity(new Intent(this, act));
    }

    public void backToSettings(View v) {
        finish();
    }
}