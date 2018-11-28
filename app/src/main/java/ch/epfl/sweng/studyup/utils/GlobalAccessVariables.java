package ch.epfl.sweng.studyup.utils;

import android.app.Activity;
import android.location.Location;

import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.WeekViewLoader;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.R;

@SuppressWarnings("HardCodedStringLiteral")
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

    public static Class HOME_ACTIVITY = null;
    public static int APP_THEME = R.style.AppTheme;

    // Schedule variables
    public static final WeekViewLoader WEEK_VIEW_LOADER = new WeekViewLoader() {
        @Override
        public double toWeekViewPeriodIndex(Calendar instance) {
            return 0;
        }

        @Override
        public List<? extends WeekViewEvent> onLoad(int periodIndex) {
            return new ArrayList<>();
        }};

}
