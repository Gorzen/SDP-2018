package ch.epfl.sweng.studyup.settings;

import android.app.AlertDialog;
import android.app.job.JobScheduler;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.caching.FileCacher;

import java.io.IOException;
import java.util.List;

import ch.epfl.sweng.studyup.LoginActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.ManageCourseActivity;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
import ch.epfl.sweng.studyup.utils.RefreshContext;

import static ch.epfl.sweng.studyup.player.HomeActivity.clearCacheToLogOut;
import static ch.epfl.sweng.studyup.utils.Constants.LANG_SETTINGS_KEYWORD;
import static ch.epfl.sweng.studyup.utils.Constants.NPC_INTERACTION_FILENAME;
import static ch.epfl.sweng.studyup.utils.Constants.PERSIST_LOGIN_FILENAME;
import static ch.epfl.sweng.studyup.utils.Constants.USER_PREFS;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;
import static ch.epfl.sweng.studyup.utils.Utils.setLocale;

public class SettingsActivity extends RefreshContext {
    private FileCacher<Boolean> enableNPCInteraction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        enableNPCInteraction = new FileCacher<>(SettingsActivity.this, NPC_INTERACTION_FILENAME);
        setCheckBoxNPC();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Player.get().isTeacher()) {
            ((TextView) findViewById(R.id.textCourseButton)).setText(R.string.course_choice_settings_button_teacher);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            enableNPCInteraction.writeCache(GlobalAccessVariables.NPCInteractionState);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setCheckBoxNPC() {
        CheckBox NPCCheckBox = findViewById(R.id.NPCInteractioncheckBox);
        final float shift = this.getResources().getDisplayMetrics().density; //Drawable of checkBox can change according to device
        NPCCheckBox.setPadding(NPCCheckBox.getPaddingLeft() + (int)(19.0f * shift + 0.5f),
                NPCCheckBox.getPaddingTop(),
                NPCCheckBox.getPaddingRight(),
                NPCCheckBox.getPaddingBottom());
        NPCCheckBox.setChecked(GlobalAccessVariables.NPCInteractionState);
    }

    public void onLogoutClick(View view) {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(BackgroundLocation.BACKGROUND_LOCATION_ID);
        clearCacheToLogOut(SettingsActivity.this);
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onChangeRoleClick(View v) {
        Intent i;
        if(Player.get().isTeacher()) {
            Player.get().setRole(Constants.Role.student);
            setRoleInCache(Constants.Role.student);
            i = new Intent(SettingsActivity.this, HomeActivity.class);
        } else {
            Player.get().setRole(Constants.Role.teacher);
            setRoleInCache(Constants.Role.teacher);
            i = new Intent(SettingsActivity.this, QuestsActivityTeacher.class);
        }

        startActivity(i);
    }

    private void setRoleInCache(Constants.Role r) {
        try {
            FileCacher<List<String>> userDataCache = new FileCacher<>(this, PERSIST_LOGIN_FILENAME);
            if (userDataCache.hasCache()) {
                final List<String> playerCacheData = userDataCache.readCache();
                playerCacheData.set(3, r.name());
                userDataCache.writeCache(playerCacheData);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                        .putString(LANG_SETTINGS_KEYWORD, lang)
                        .apply();
                setLocale(lang, MOST_RECENT_ACTIVITY);

                Class nextActivity = Player.get().isTeacher() ?
                        QuestsActivityTeacher.class : HomeActivity.class;
                startActivity(new Intent(MOST_RECENT_ACTIVITY, nextActivity));
            }
        });
        languageChoiceBuilder.setNegativeButton(R.string.cancel, null);
        languageChoiceBuilder.create().show();
    }

    public void onCourseChoiceClick(View view) {
        Intent intent = Player.get().isStudent() ?
                new Intent(SettingsActivity.this, CourseSelectionActivity.class)
                : new Intent(SettingsActivity.this, ManageCourseActivity.class);
        startActivity(intent);
    }

    public void onBackButton(View view) {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void onNPCInteractionStateChange(View view){
        GlobalAccessVariables.NPCInteractionState = !GlobalAccessVariables.NPCInteractionState;
    }
}
