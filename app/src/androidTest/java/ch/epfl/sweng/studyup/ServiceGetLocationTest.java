package ch.epfl.sweng.studyup;

import android.app.job.JobParameters;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.map.BackgroundLocation;

import static org.junit.Assert.assertNull;


@RunWith(AndroidJUnit4.class)
public class ServiceGetLocationTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule2 =
            new ActivityTestRule<>(MainActivity.class);

    /*
    @Test
    public void backgroundLocationDoesntCrashWithBadParams() {
        BackgroundLocation backgroundLocation = new BackgroundLocation();
        //backgroundLocation.onStartJob(null);
        //backgroundLocation.onStopJob(null);

        BackgroundLocation.GetLocation getLocation = backgroundLocation.new GetLocation(null, null);
        //getLocation.doInBackground(new Void[]{});
        //getLocation.onPostExecute(null);
        getLocation.cancel(true);
        Log.d("GPS_TEST", "background = " + backgroundLocation);
        backgroundLocation.onStopJob(null);
        Log.d("GPS_TEST", "background = " + backgroundLocation);
    }
    */

    @Test
    public void getLocationNoCrashWithBadParams() {
        BackgroundLocation.GetLocation getLocation = new BackgroundLocation.GetLocation(null, null);

        JobParameters jobParameters = getLocation.doInBackground(new Void[]{});
        assertNull(jobParameters);

        getLocation.onPostExecute(null);
    }
}
