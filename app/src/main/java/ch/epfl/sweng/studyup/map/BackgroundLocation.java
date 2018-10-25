package ch.epfl.sweng.studyup.map;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.lang.ref.WeakReference;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Rooms;
import ch.epfl.sweng.studyup.utils.Utils;

/**
 * BackgroundLocation
 * <p>
 * Asynchronouly checks for the localisation in background.
 */
public class BackgroundLocation extends JobService {
    public static int BACKGROUND_LOCATION_ID = 143;

    public BackgroundLocation() {
        //Log.d("GPS_MAP", "Created background location, default constructor");
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        //Log.d("GPS_MAP", "Started job");
        new GetLocation(this, jobParameters).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if(jobParameters != null) {
            jobFinished(jobParameters, false);
        }
        return false;
    }

    public static class GetLocation extends AsyncTask<Void, Void, JobParameters> {
        private final WeakReference<JobService> jobService;
        private final JobParameters jobParameters;
        private final WeakReference<Context> context;

        public GetLocation(JobService jobService, JobParameters jobParameters) {
            this.jobService = new WeakReference<>(jobService);
            this.jobParameters = jobParameters;
            this.context = new WeakReference<>(Utils.mainContext);
        }

        @Override
        public JobParameters doInBackground(Void[] voids) {
            if(context.get() == null){
                return jobParameters;
            }
            if (ContextCompat.checkSelfPermission(context.get(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(context.get(),
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("GPS_MAP", "Permission granted");
                Utils.locationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    //Log.d("GPS_MAP", "NEW POS: Latitude = " + location.getLatitude() + "  Longitude: " + location.getLongitude());
                                    Utils.position = new LatLng(location.getLatitude(), location.getLongitude());
                                    if(Rooms.checkIfUserIsInRoom(Player.get().getCurrentRoom())) {
                                        //A voir
                                    }
                                    //Log.d("GPS_MAP", "Changed position of Main Activity " + location);
                                } else {
                                    //Log.d("GPS_MAP", "NEW POS: null");
                                }
                            }
                        });
            } else {
                //Log.d("GPS_MAP", "No permission");
            }

            return jobParameters;
        }

        @Override
        public void onPostExecute(JobParameters jobParameters) {
            super.onPostExecute(jobParameters);
            if(jobParameters != null && jobService.get() != null) {
                jobService.get().jobFinished(jobParameters, true);
            }
        }
    }
}
