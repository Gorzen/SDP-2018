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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public class GetLocation extends AsyncTask<Object, Void, JobParameters> {
    private LatLng latLong = null;
    private final JobService jobService;
    private final JobParameters jobParameters;
    public static ArrayList<GetLocationListener> observers = new ArrayList<>();

    public GetLocation(JobService jobService, JobParameters jobParameters){
        this.jobService = jobService;
        this.jobParameters = jobParameters;
    }

    @Override
    protected JobParameters doInBackground(Object[] objects) {
        assert(objects.length == 2);
        assert(objects[0] instanceof FusedLocationProviderClient);
        assert(objects[1] instanceof Context);

        FusedLocationProviderClient locationProviderClient = (FusedLocationProviderClient) objects[0];
        final Context context = (Context) objects[1];

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        Log.d("GPS_MAP", "NEW POS: Latitude = " + location.getLatitude() + "  Longitude: " + location.getLongitude());
                        latLong = new LatLng(location.getLatitude(), location.getLongitude());
                    }else{
                        Log.d("GPS_MAP", "NEW POS: null");
                    }
                }
            });
        }

        if(latLong != null){
            for(GetLocationListener listener : observers){
                if(listener != null){
                    listener.onLocationUpdate(new LatLng(latLong.latitude, latLong.longitude));
                }
            }
        }

        return jobParameters;
    }

    @Override
    protected void onPostExecute(JobParameters jobParameters) {
        super.onPostExecute(jobParameters);
        jobService.jobFinished(jobParameters, false);
    }
}
