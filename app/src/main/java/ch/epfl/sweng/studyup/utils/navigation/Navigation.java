package ch.epfl.sweng.studyup.utils.navigation;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.utils.RefreshContext;
import ch.epfl.sweng.studyup.utils.Utils;


/**
 * Navigation class that serve as basic interface for navigation between activities
 *
 */
public abstract class Navigation extends RefreshContext implements ActivityCompat
        .OnRequestPermissionsResultCallback {
    protected ArrayList<Integer> buttonIds = null;
    protected ArrayList<Class> activities = null;
    protected ArrayList<Integer> activitiesId = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupNavigation();
    }

    public void navigationSwitcher(final Context cn, final Class<?> activity, final int current_index) {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();

        MenuItem menuItem = menu.getItem(current_index);

        menuItem.setChecked(false); // Give color to the selected item

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                for (int i = 0; i < buttonIds.size(); i++) {   //all three lists should have the same size
                    if (item.getItemId() == buttonIds.get(i) && !activity.equals(activities.get(i))) {
                        Intent intent = new Intent(cn, activities.get(i));
                        cn.startActivity(intent);
                        transitionForNavigation(current_index, activitiesId.get(i));
                    }
                }
                return false;
            }
        });
    }

    public void transitionForNavigation(int current_index, int destination_index) {
        if (destination_index > current_index) {
            overridePendingTransition(R.anim.go_right_in, R.anim.go_right_out);
        } else if (destination_index < current_index) {
            overridePendingTransition(R.anim.go_left_in, R.anim.go_left_out);
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

    /**
     * Method that set the correct data to buttonIds, activities and activitiesIds
     */
    protected abstract void setupNavigation();
}
