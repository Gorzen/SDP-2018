package ch.epfl.sweng.studyup.settings;

import android.app.AlertDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import ch.epfl.sweng.studyup.LoginActivity;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.RefreshContext;

import static ch.epfl.sweng.studyup.player.HomeActivity.clearCacheToLogOut;
import static ch.epfl.sweng.studyup.utils.Constants.USER_PREFS;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;
import static ch.epfl.sweng.studyup.utils.Utils.setLocale;

public class SettingsActivity extends RefreshContext {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public void onLogoutClick(View view) {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(BackgroundLocation.BACKGROUND_LOCATION_ID);
        clearCacheToLogOut(SettingsActivity.this);
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onLanguageChoiceClick(View view) {
        AlertDialog.Builder languageChoiceBuilder = new AlertDialog.Builder(this);
        languageChoiceBuilder.setTitle(R.string.language_title_alert_dialog);
        languageChoiceBuilder.setItems(Constants.LANGUAGES, new DialogInterface.OnClickListener() {
            @SuppressWarnings("HardCodedStringLiteral")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String lang = "en"; // Basis
                if(which == 0) {
                    lang = "en";
                    Toast.makeText(SettingsActivity.this, getString(R.string.text_langen), Toast.LENGTH_SHORT).show();
                } else if(which == 1) {
                    lang = "fr";
                    Toast.makeText(SettingsActivity.this, getString(R.string.text_langfr), Toast.LENGTH_SHORT).show();
                }
                getSharedPreferences(USER_PREFS, MODE_PRIVATE).edit()
                        .putString("lang", lang)
                        .apply();
                setLocale(lang, MOST_RECENT_ACTIVITY);

                Class nextActivity = Player.get().getRole() == Constants.Role.teacher ?
                        QuestsActivityTeacher.class : HomeActivity.class;
                startActivity(new Intent(MOST_RECENT_ACTIVITY, nextActivity));
            }
        });
        languageChoiceBuilder.setNegativeButton(R.string.cancel, null);
        languageChoiceBuilder.create().show();
    }

    public void onCourseChoiceClick(View view) {
        Intent intent = new Intent(SettingsActivity.this, CourseSelectionActivity.class);
        startActivity(intent);
    }

    public void onBackButton(View view) {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onColorChoiceClick(View view){
        Intent intent = new Intent(SettingsActivity.this, ChooseColorActivity.class);
        startActivity(intent);
    }
}
