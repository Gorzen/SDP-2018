package ch.epfl.sweng.studyup.map;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
                Log.d("GPS_MAP", "Context = null");
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
                                    String str = "NEW POS: " + Utils.position.latitude + ", " + Utils.position.longitude;
                                    if(Rooms.checkIfUserIsInRoom(Player.get().getCurrentRoom())) {
                                        str += '\n' + "You are in your room: " + Player.get().getCurrentRoom();
                                        Player.get().addExperience(2*Utils.XP_STEP, context.get());
                                    }else{
                                        str += '\n' + "You are not in your room: " + Player.get().getCurrentRoom();

                                    }
                                    Toast.makeText(context.get(), str, Toast.LENGTH_SHORT).show();
                                    Log.d("GPS_MAP", str);
                                    Log.d("GPS_MAP", "Context = " + context.get());
                                } else {
                                    Log.d("GPS_MAP", "NEW POS: null");
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
