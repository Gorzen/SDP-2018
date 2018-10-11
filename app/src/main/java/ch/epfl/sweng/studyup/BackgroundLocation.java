package ch.epfl.sweng.studyup;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;

public class BackgroundLocation extends JobService {
    private final FusedLocationProviderClient locationProviderClient;
    private final Context context;
    public static int BACKGROUND_LOCATION_ID = 132;

    /*
    public BackgroundLocation(Activity activity){
        locationProviderClient = new FusedLocationProviderClient(activity);
        this.context = activity.getApplicationContext();
        Log.d("GPS_MAP", "Created background location, normal constructor");
    }
    */

    public BackgroundLocation(){
        context = null;
        locationProviderClient = null;
        Log.d("GPS_MAP", "Created background location, default constructor");
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d("GPS_MAP", "Started job");
        new GetLocation(this, jobParameters).execute(new Object[]{locationProviderClient, context});
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
