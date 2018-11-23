package ch.epfl.sweng.studyup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.kosalgeek.android.caching.FileCacher;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import ch.epfl.sweng.studyup.firebase.FileStorage;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestDisplayActivity;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestNQuestions;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuest;
import ch.epfl.sweng.studyup.utils.adapters.SpecialQuestListViewAdapter;
import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import static ch.epfl.sweng.studyup.utils.Constants.MAIN_INDEX;
import static ch.epfl.sweng.studyup.utils.Constants.PERSIST_LOGIN_FILENAME;
import static ch.epfl.sweng.studyup.utils.Constants.SPECIAL_QUEST_KEY;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.LOCATION_PROVIDER_CLIENT;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOCK_ENABLED;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

public class MainActivity extends NavigationStudent {
    private final int MY_PERMISSION_REQUEST_FINE_LOCATION = 202;
    private ImageView image_view;

    // Text that will be displayed in the levelProgress layout
    CircularProgressIndicator levelProgress;
    ImageButton pic_button2;
    ImageButton pic_button;

    // Display login success message from intent set by authentication activity
    public void displayLoginSuccessMessage(Intent intent) {
        String successMessage = intent.getStringExtra(getString(R.string.post_login_message_value));
        if (successMessage != null) {
            Toast.makeText(getApplicationContext(), successMessage, Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("GPS_MAP", "Destroyed main and canceled Background location service");
        unScheduleBackgroundLocation();
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayLoginSuccessMessage(getIntent());

        if (!MOCK_ENABLED) {
            Firestore.get().loadQuestions(this);
        }

        pic_button = findViewById(R.id.pic_btn);
        pic_button2 = findViewById(R.id.pic_btn2);

        Log.d("GPS_MAP", "Started main");
        // GPS Job scheduler
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSION_REQUEST_FINE_LOCATION);

        MOST_RECENT_ACTIVITY = this;
        LOCATION_PROVIDER_CLIENT = new FusedLocationProviderClient(this);

        if (!MOCK_ENABLED) {
            scheduleBackgroundLocation();
        }

        //bottom navigation bar
        navigationSwitcher(MainActivity.this, MainActivity.class, MAIN_INDEX);

        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                123);

        loadInterface();
        populateSpecialQuestsList();
    }

    private void loadInterface() {
        // User picture
        ImageButton pic_button = findViewById(R.id.pic_btn);
        pic_button2 = findViewById(R.id.pic_btn2);
        image_view = findViewById(R.id.pic_imageview);

        FileStorage.downloadProfilePicture(Player.get().getSciperNum(), image_view);

        pic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.go_right_in, R.anim.go_right_out);
            }
        });
        pic_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.go_right_in, R.anim.go_right_out);
                pic_button2.setBackground(getResources().getDrawable(R.drawable.ic_mode_edit_clicked_24dp));
            }
        });

        //username
        TextView view_username = findViewById(R.id.usernameText);
        view_username.setText(Player.get().getUserName());
        view_username.setMaxLines(1);
        view_username.setMaxWidth(300);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        levelProgress = findViewById(R.id.level_progress);
        levelProgress.setProgress(Player.get().getLevelProgress(), 1);
        levelProgress.setStartAngle(270);
        updateCurrDisplay();
        updateXpAndLvlDisplay();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateUsernameDisplay();
        updateCurrDisplay();
        updateXpAndLvlDisplay();
        populateSpecialQuestsList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("GPS_MAP", "Permission granted");
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.location_required), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    public void scheduleBackgroundLocation(){
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(BackgroundLocation.BACKGROUND_LOCATION_ID, new ComponentName(this, BackgroundLocation.class)).setPeriodic(15 * 60 * 1000).build();
        scheduler.schedule(jobInfo);
        for(JobInfo job: scheduler.getAllPendingJobs()){
            Log.d("GPS_MAP", "Scheduled: " + job);
        }
        Log.d("GPS_MAP", "schedule");
    }
    public void unScheduleBackgroundLocation(){
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(BackgroundLocation.BACKGROUND_LOCATION_ID);
    }

    public void updateUsernameDisplay() {
        TextView view_username = findViewById(R.id.usernameText);
        view_username.setText(Player.get().getUserName());
        view_username.setMaxLines(1);
        view_username.setMaxWidth(300);
    }

    public void updateXpAndLvlDisplay() {
        levelProgress.setCurrentProgress(Player.get().getLevelProgress());
        ((TextView) findViewById(R.id.levelText)).setText(getString(R.string.text_level) + Player.get().getLevel());
    }

    /*
    Unfinished method to populate new view for special quests.
    For consistency, it uses largely the same architecture as QuestsActivityStudent.
     */
    public void populateSpecialQuestsList() {

        /*
        As a proof of concept, the list view will be populated with one sample special quest.
        This will be a SpecialQuestNQuestions object.
         */

        final List<SpecialQuest> specialQuestsList = Player.get().getActiveQuests();

        /*
        Image id list to store icon for special quest (finished vs unfinished).
        The item at index i corresponds to the special quest at index i in specialQuestList.
        As a proof of concept it is populated with one unfinished icon.
         */
        List<Integer> iconList = new ArrayList<>();
        iconList.add(R.drawable.ic_todo_grey_24dp);

        ListView specialQuestsListView = findViewById(R.id.specialQuestsListView);
        SpecialQuestListViewAdapter listAdapter =
            new SpecialQuestListViewAdapter(this, R.layout.special_quest_model, specialQuestsList, iconList);
        specialQuestsListView.setAdapter(listAdapter);

        /*
        Set onClick listeners for all special quests that will open SpecialQuestDisplayActivity,
        passing the Special Quest as a serializable object. It will be de-serialized and used in that activity.
         */
        specialQuestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent displaySpecialQuestion = new Intent(MainActivity.this, SpecialQuestDisplayActivity.class);
                displaySpecialQuestion.putExtra(SPECIAL_QUEST_KEY, specialQuestsList.get(position));

                startActivity(displaySpecialQuestion);
            }});
    }

    public void updateCurrDisplay() {
        ((TextView) findViewById(R.id.currText)).setText(getString(R.string.text_money) + Player.get().getCurrency());
    }

    public static void clearCacheToLogOut(Context context) {
        FileCacher<String[]> persistLogin = new FileCacher<>(context, PERSIST_LOGIN_FILENAME);
        try {
            persistLogin.clearCache();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}