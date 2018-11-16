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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.kosalgeek.android.caching.FileCacher;

import java.io.IOException;

import ch.epfl.sweng.studyup.firebase.FileStorage;
import ch.epfl.sweng.studyup.firebase.Firestore;

import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.player.CustomActivity;
import ch.epfl.sweng.studyup.player.Player;

import ch.epfl.sweng.studyup.utils.navigation.NavigationStudent;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;

import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.*;

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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("GPS_MAP", "Permission granted");
                } else {
                    Toast.makeText(getApplicationContext(), "This app requires location", Toast.LENGTH_SHORT).show();
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
        ((TextView) findViewById(R.id.levelText)).setText(LEVEL_DISPLAY + Player.get().getLevel());
    }

    public void updateCurrDisplay() {
        ((TextView) findViewById(R.id.currText)).setText(CURR_DISPLAY + Player.get().getCurrency());
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


