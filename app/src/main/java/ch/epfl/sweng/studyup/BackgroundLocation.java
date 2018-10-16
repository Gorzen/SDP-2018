package ch.epfl.sweng.studyup;

import android.Manifest;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

public class BackgroundLocation extends JobService {
    private final Context context;
    public static int BACKGROUND_LOCATION_ID = 0;

    public BackgroundLocation(){
        Log.d("GPS_MAP", "Created background location, default constructor");
        this.context = Utils.mainContext;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("GPS_MAP", "Started job");
        new GetLocation(this, jobParameters).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }



    private class GetLocation extends AsyncTask<Void, Void, JobParameters> {
        private final JobService jobService;
        private final JobParameters jobParameters;

        public GetLocation(JobService jobService, JobParameters jobParameters){
            this.jobService = jobService;
            this.jobParameters = jobParameters;
        }

        @Override
        protected JobParameters doInBackground(Void[] voids) {
            if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d("GPS_MAP", "Permission granted");
                Utils.locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            Log.d("GPS_MAP", "NEW POS: Latitude = " + location.getLatitude() + "  Longitude: " + location.getLongitude());
                            Utils.position = new LatLng(location.getLatitude(), location.getLongitude());
                            Log.d("GPS_MAP", "Changed position of Main Activity " + location);
                        } else {
                            Log.d("GPS_MAP", "NEW POS: null");
                        }
                    }
                });
            }else{
                Log.d("GPS_MAP", "No permission");
            }

            return jobParameters;
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            super.onPostExecute(jobParameters);
            jobService.jobFinished(jobParameters, true);
        }


    }
}
