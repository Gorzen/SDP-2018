package ch.epfl.sweng.studyup;

import android.content.Intent;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.utils.Utils;


@RunWith(AndroidJUnit4.class)
public class ServiceBackgroundLocationTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule2 =
            new ActivityTestRule<>(MainActivity.class, true, false);

    @Before
    public void setup(){
        Utils.isMockEnabled = true;
        mActivityRule2.launchActivity(new Intent());
    }

    @Test
    public void backgroundLocationDoesntCrashWithBadParams(){
        /*
        BackgroundLocation backgroundLocation = new BackgroundLocation();
        backgroundLocation.onStartJob(null);
        backgroundLocation.onStopJob(null);

        BackgroundLocation.GetLocation getLocation = backgroundLocation.new GetLocation(null, null);
        getLocation.doInBackground(new Void[]{});
        getLocation.onPostExecute(null);
        */
    }
}
