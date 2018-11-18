package ch.epfl.sweng.studyup;

import android.app.AlertDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.RefreshContext;

import static ch.epfl.sweng.studyup.MainActivity.clearCacheToLogOut;

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
                if(which == 0) {
                    Toast.makeText(SettingsActivity.this, getString(R.string.text_langen), Toast.LENGTH_SHORT).show();
                } else if(which == 1) {
                    Toast.makeText(SettingsActivity.this, getString(R.string.text_langfr), Toast.LENGTH_SHORT).show();
                }
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
