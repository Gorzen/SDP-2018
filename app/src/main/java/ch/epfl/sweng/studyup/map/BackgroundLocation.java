package ch.epfl.sweng.studyup.map;

import android.Manifest;
import android.app.Activity;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Rooms;

import static ch.epfl.sweng.studyup.utils.Constants.XP_STEP;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.LOCATION_PROVIDER_CLIENT;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.POSITION;

/**
 * BackgroundLocation
 * <p>
 * Asynchronouly checks for the localisation in background.
 */
public class BackgroundLocation extends JobService {
    public static int BACKGROUND_LOCATION_ID = 143;

    public BackgroundLocation() {
        Log.d("GPS_MAP", "Created background location, default constructor");
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //Log.d("GPS_MAP", "Started job");
        new GetLocation(this, jobParameters).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (jobParameters != null) {
            jobFinished(jobParameters, false);
        }
        return false;
    }

    public static class GetLocation extends AsyncTask<Void, Void, JobParameters> {
        private final WeakReference<JobService> jobService;
        private final JobParameters jobParameters;
        private final WeakReference<Activity> activity;
        public final OnSuccessListener<Location> onSuccessListener = new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Log.d("GPS_MAP", "NEW POS: Latitude = " + location.getLatitude() + "  Longitude: " + location.getLongitude());
                    POSITION = new LatLng(location.getLatitude(), location.getLongitude());
                    String str = "NEW POS: " + POSITION.latitude + ", " + POSITION.longitude;
                    if (Rooms.checkIfUserIsInRoom()) {
                        str += '\n' + "You are in your room: ";
                        Player.get().addExperience(2 * XP_STEP, activity.get());
                    } else {
                        str += '\n' + "You are not in your room: ";
                    }
                    //Toast.makeText(context.get(), str, Toast.LENGTH_SHORT).show();
                    Log.d("GPS_MAP", str);
                    Log.d("GPS_MAP", "Context = " + activity.get());
                } else {
                    Log.d("GPS_MAP", "NEW POS: null");
                }
            }
        };

        public GetLocation(JobService jobService, JobParameters jobParameters) {
            this.jobService = new WeakReference<>(jobService);
            this.jobParameters = jobParameters;
            this.activity = new WeakReference<>(MOST_RECENT_ACTIVITY);
        }

        @Override
        public JobParameters doInBackground(Void[] voids) {
            if (activity.get() == null) {
                Log.d("GPS_MAP", "Context = null");
                return jobParameters;
            }
            if (ContextCompat.checkSelfPermission(activity.get(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(activity.get(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("GPS_MAP", "Permission granted");
                LOCATION_PROVIDER_CLIENT.getLastLocation()
                        .addOnSuccessListener(onSuccessListener);
            }

            return jobParameters;
        }

        @Override
        public void onPostExecute(JobParameters jobParameters) {
            super.onPostExecute(jobParameters);
            if (jobParameters != null && jobService.get() != null) {
                jobService.get().jobFinished(jobParameters, true);
            }
        }
    }
}
