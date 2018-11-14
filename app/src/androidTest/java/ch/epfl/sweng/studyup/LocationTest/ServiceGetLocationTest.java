package ch.epfl.sweng.studyup.LocationTest;

import android.app.job.JobParameters;
import android.location.Location;
import android.os.SystemClock;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.MainActivity;
import ch.epfl.sweng.studyup.map.BackgroundLocation;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Rooms;
import ch.epfl.sweng.studyup.utils.Utils;

import static ch.epfl.sweng.studyup.utils.Constants.XP_STEP;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.POSITION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


@RunWith(AndroidJUnit4.class)
public class ServiceGetLocationTest {
    private BackgroundLocation.GetLocation getLocation = new BackgroundLocation.GetLocation(null, null);
    private OnSuccessListener<Location> onSuccessListener = getLocation.onSuccessListener;
    private LatLng notRoomOfPlayer = new LatLng(50.0, 10.0);

    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule2 =
            new ActivityTestRule<>(MainActivity.class);

    @Test
    public void getLocationNoCrashWithBadParams() {
        BackgroundLocation.GetLocation getLocation = new BackgroundLocation.GetLocation(null, null);

        JobParameters jobParameters = getLocation.doInBackground(new Void[]{});
        assertNull(jobParameters);

        getLocation.onPostExecute(null);
    }

    @Test
    public void backgroundLocationNoCrashWithBadParams(){
        BackgroundLocation backgroundLocation = new BackgroundLocation();
        backgroundLocation.onStartJob(null);
        backgroundLocation.onStopJob(null);
    }

    @Test
    public void onSuccessListenerTestNullLocation(){
        onSuccessListener.onSuccess(null);
    }

    @Test
    public void onSuccessListenerTestRoomOfPlayer(){
        final int exp = Player.get().getExperience();
        final LatLng roomOfPlayer = Rooms.ROOMS_LOCATIONS.get(Player.get().getCurrentRoom()).getLocation();

        final Location location = new Location("Mock");
        location.setLatitude(roomOfPlayer.latitude);
        location.setLongitude(roomOfPlayer.longitude);
        location.setAccuracy(1);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        location.setTime(System.currentTimeMillis());

        mActivityRule2.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccessListener.onSuccess(location);
                assertEquals(roomOfPlayer.latitude, POSITION.latitude, 0);
                assertEquals(roomOfPlayer.longitude, POSITION.longitude, 0);
                assertEquals(exp + 2 * XP_STEP, Player.get().getExperience());
            }
        });
    }

    @Test
    public void onSuccessListenerTestNotRoomOfPlayer(){
        final int exp = Player.get().getExperience();

        final Location location = new Location("Mock");
        location.setLatitude(notRoomOfPlayer.latitude);
        location.setLongitude(notRoomOfPlayer.longitude);
        location.setAccuracy(1);
        location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        location.setTime(System.currentTimeMillis());

        mActivityRule2.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onSuccessListener.onSuccess(location);
                assertEquals(notRoomOfPlayer.latitude, POSITION.latitude, 0);
                assertEquals(notRoomOfPlayer.longitude, POSITION.longitude, 0);
                assertEquals(exp, Player.get().getExperience());
            }
        });
    }
}
