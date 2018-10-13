package ch.epfl.sweng.studyup;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import ch.epfl.sweng.studyup.question.AddQuestionActivity;

import static android.support.v4.app.ActivityCompat.requestPermissions;


public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    CircularProgressIndicator levelProgress;
    private final int MY_PERMISSION_REQUEST_FINE_LOCATION = 202;

    //Texte that will be displayed in the levelProgress layout
    private static final CircularProgressIndicator.ProgressTextAdapter LEVEL_PROGRESS_TEXT = new CircularProgressIndicator.ProgressTextAdapter() {
        @Override
        public String formatText(double progress) {
            return (progress * 100 + "% of level ").concat(String.valueOf(Player.get().getLevel() + 1));
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(BackgroundLocation.BACKGROUND_LOCATION_ID);
        Log.d("GPS_MAP", "Destroyed main and canceled Background location service");
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("GPS_MAP", "Started main");
        //GPS Job scheduler
        ActivityCompat.requestPermissions(
                MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                MY_PERMISSION_REQUEST_FINE_LOCATION);
        Utils.mainContext = this.getApplicationContext();
        Utils.locationProviderClient = new FusedLocationProviderClient(this);
        if (Utils.isMockEnabled) {
            Utils.locationProviderClient.setMockMode(true);
            Utils.locationProviderClient.setMockLocation(Utils.mockLoc);
            Log.d("GPS_MAP", "Mock location set");
        }
        JobScheduler scheduler = (JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo jobInfo = new JobInfo.Builder(BackgroundLocation.BACKGROUND_LOCATION_ID, new ComponentName(this, BackgroundLocation.class)).setPeriodic(15 * 60 * 1000).build();
        scheduler.schedule(jobInfo);
        for(JobInfo job : scheduler.getAllPendingJobs()){
            Log.d("GPS_MAP", job.toString());
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true); //give color to the selected item

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        break;

                    case R.id.navigation_quests:
                        Intent intent_q = new Intent(MainActivity.this, QuestsActivity.class);
                        startActivity(intent_q);
                        //todo R.anim.### does not work if we want better transitions
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_rankings:
                        Intent intent_r = new Intent(MainActivity.this, RankingsActivity.class);
                        startActivity(intent_r);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_map:
                        Intent intent_m = new Intent(MainActivity.this, MapsActivity.class);
                        startActivity(intent_m);
                        overridePendingTransition(0, 0);
                        break;

                    case R.id.navigation_chat:
                        Intent intent_c = new Intent(MainActivity.this, ChatActivity.class);
                        startActivity(intent_c);
                        overridePendingTransition(0, 0);
                        break;

                    default:
                        return false;
                }
                return false;
            }
        });

        levelProgress = findViewById(R.id.level_progress);
        levelProgress.setProgress(Player.get().getLevelProgress(), 1);
        levelProgress.setStartAngle(270);
        levelProgress.setProgressTextAdapter(LEVEL_PROGRESS_TEXT);
    }

    //Display the toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater i = getMenuInflater();
        i.inflate(R.menu.top_navigation, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("GPS_MAP", "Permission granted");
                } else {
                    Toast.makeText(getApplicationContext(), "This app requires location", Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;
        }
    }

    //Allows you to do an action with the toolbar (in a different way than with the navigation bar)
    //Corresponding activities are not created yet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.top_navigation_settings) {
            Toast.makeText(MainActivity.this,
                    "You have clicked on Settings :)",
                    Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.top_navigation_infos) {
            Toast.makeText(MainActivity.this,
                    "You have clicked on Infos :)",
                    Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void questionAddTest(View view) {
        Intent intent = new Intent(this, AddQuestionActivity.class);
        startActivity(intent);
    }

    public void onLocButtonClick(View view) {
        LocationTracker locationTracker = new LocationTracker(getApplicationContext());
        Location currLoc = locationTracker.getLocation();
        if (currLoc != null) {
            double currLat = currLoc.getLatitude();
            double currLong = currLoc.getLongitude();
            Toast.makeText(getApplicationContext(),
                    "Latitude: " + currLat + "\nLongitude: " + currLong,
                    Toast.LENGTH_LONG).show();
            TextView test = findViewById(R.id.helloWorld);
            test.setText("Latitude: " + currLat + "\nLongitude: " + currLong);  //TODO not recommended to concatenate here, should use resource string with placeholders (+hardcoded literals)
        }
    }

    /**
     * Function that is called when adding xp with the button
     *
     * @param view
     */
    public void addExpPlayer(View view) {
        Player.get().addExperience(Player.XP_STEP);
        levelProgress.setCurrentProgress(Player.get().getLevelProgress());
        levelProgress.setProgressTextAdapter(LEVEL_PROGRESS_TEXT);
    }
}
