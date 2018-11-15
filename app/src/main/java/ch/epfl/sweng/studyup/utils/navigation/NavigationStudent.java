package ch.epfl.sweng.studyup.utils.navigation;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.SettingsActivity;
import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.map.MapsActivity;
import ch.epfl.sweng.studyup.player.QuestsActivityStudent;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.social.RankingsActivity;
import static ch.epfl.sweng.studyup.utils.Constants.*;

public class NavigationStudent extends Navigation {
    public final ArrayList<Integer> buttonIdsStudent = new ArrayList<>(Arrays.asList(
            R.id.navigation_home,
            R.id.navigation_quests_student,
            R.id.navigation_rankings,
            R.id.navigation_map,
            R.id.navigation_inventory));

    private final ArrayList<Class> activitiesStudent = new ArrayList<Class>(Arrays.asList(
            MainActivity.class,
            QuestsActivityStudent.class,
            RankingsActivity.class,
            MapsActivity.class,
            InventoryActivity.class));

    private final ArrayList<Integer> activitiesIdStudent  = new ArrayList<>(Arrays.asList(
            MAIN_INDEX,
            QUESTS_INDEX_STUDENT,
            RANKINGS_INDEX,
            MAP_INDEX,
            INVENTORY_INDEX));

    @Override
    protected void setupNavigation() {
        buttonIds = buttonIdsStudent;
        activities = activitiesStudent;
        activitiesId = activitiesIdStudent;
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
}
