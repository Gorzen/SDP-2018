package ch.epfl.sweng.studyup;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.studyup.map.Room;
import ch.epfl.sweng.studyup.utils.Rooms;
import ch.epfl.sweng.studyup.utils.Utils;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class RoomsTest {

    @Test
    public void computeDistanceTest(){
        double distance = Rooms.distanceBetweenTwoLatLng(Rooms.ROOMS_LOCATIONS.get("INR_0_11").getLocation(), Rooms.ROOMS_LOCATIONS.get("CO_1_1").getLocation());
        assertEquals(256.9647560362717, distance, 1e-4);
    }

    @Test
    public void checkIfUserIsInRoom(){
        Utils.position = Rooms.ROOMS_LOCATIONS.get("CO_1_1").getLocation();
        assertEquals(true, Rooms.checkIfUserIsInRoom("CO_1_1"));

        Utils.position = new LatLng(46.5185307, 6.5619707);
        assertEquals(true, Rooms.checkIfUserIsInRoom("BC_0_0"));

        Utils.position = new LatLng(0, 0);
        assertEquals(false, Rooms.checkIfUserIsInRoom("CE_1_6"));

        Utils.position = new LatLng(45, 6);
        assertEquals(false, Rooms.checkIfUserIsInRoom("CM_1_4"));
    }
}
