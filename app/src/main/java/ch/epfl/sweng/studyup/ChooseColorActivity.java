package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
import ch.epfl.sweng.studyup.utils.RefreshContext;

public class ChooseColorActivity extends RefreshContext {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_color);
    }

    public void changeTheme(View view){
        //Change les boutons pour soit AppTheme soit AppThemeBlue
        GlobalAccessVariables.APP_THEME = R.style.AppThemeGreen;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}