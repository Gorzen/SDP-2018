package ch.epfl.sweng.studyup;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.map.BackgroundLocation;


@RunWith(AndroidJUnit4.class)
public class BackgroundLocationTest {
    /*
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule2 =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void backgroundLocationDoesntCrashWithBadParams(){
        BackgroundLocation backgroundLocation = new BackgroundLocation();
        backgroundLocation.onStartJob(null);
        backgroundLocation.onStopJob(null);

        BackgroundLocation.GetLocation getLocation = backgroundLocation.new GetLocation(null, null);
        getLocation.doInBackground(new Void[]{});
        getLocation.onPostExecute(null);
    }
    */
}
