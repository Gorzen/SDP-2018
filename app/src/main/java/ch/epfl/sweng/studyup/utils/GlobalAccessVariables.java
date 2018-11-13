package ch.epfl.sweng.studyup.utils;

import android.location.Location;

import java.util.Map;

public class GlobalAccessVariables {

    public static Boolean isMockEnabled = false;
    public static Location mockLoc = null;

    //This question's id on the server is what the tests use
    public static final String MOCK_UUID = "fake-UUID";

    public static Map<String, Object> dbStaticInfo = null;
}
