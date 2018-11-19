package ch.epfl.sweng.studyup;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Locale;

import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.RefreshContext;

import static ch.epfl.sweng.studyup.MainActivity.clearCacheToLogOut;
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

                startActivity(new Intent(MOST_RECENT_ACTIVITY, MainActivity.class));
            }
        });
        languageChoiceBuilder.setNegativeButton(R.string.cancel, null);
        languageChoiceBuilder.create().show();
    }

    public void onBackButton(View view) {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
