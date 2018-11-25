package ch.epfl.sweng.studyup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ch.epfl.sweng.studyup.utils.Rooms;

import static org.junit.Assert.assertEquals;


@RunWith(JUnit4.class)
public class RoomsTest {


    @Test
    public void computeDistanceTest() {
        double distance = Rooms.distanceBetweenTwoLatLng(Rooms.ROOMS_LOCATIONS.get("INR_0_11").getLocation(), Rooms.ROOMS_LOCATIONS.get("CO_1_1").getLocation());
        assertEquals(256.9647560362717, distance, 1e-4);
    }

}
