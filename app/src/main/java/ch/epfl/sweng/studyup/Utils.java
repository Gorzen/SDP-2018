package ch.epfl.sweng.studyup;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

public class Utils {
    public static LatLng position;
    public static FusedLocationProviderClient locationProviderClient;
    public static Context mainContext;


    //test purpose
    public static Boolean isMockEnabled = false;
    public static Location mockLoc;

}
