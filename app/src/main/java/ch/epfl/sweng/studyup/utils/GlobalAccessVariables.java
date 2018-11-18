package ch.epfl.sweng.studyup.utils;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.Map;

public abstract class GlobalAccessVariables {

    public static final String MOCK_UUID = "fake-UUID";
    public static final String MOCK_TOKEN = "NON-NULL TOKEN VALUE";
    public static Activity MOST_RECENT_ACTIVITY = null;
    public static LatLng POSITION = null;
    public static String ROOM_NUM = "INN_3_26";
    public static FusedLocationProviderClient LOCATION_PROVIDER_CLIENT = null;
    public static Boolean MOCK_ENABLED = false;
    public static Location MOC_LOC = null;

    public static Map<String, Object> DB_STATIC_INFO = null;

}
