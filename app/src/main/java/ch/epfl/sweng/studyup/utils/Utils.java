package ch.epfl.sweng.studyup.utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ch.epfl.sweng.studyup.SettingsActivity;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;

import static ch.epfl.sweng.studyup.utils.Constants.Course;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

public abstract class Utils {

    /**
     * Wait for a given action to be completed. Return an error message if any.
     *
     * @param time time in ms to wait
     * @param tag  error message if any
     */
    public static final void waitAndTag(int time, String tag) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {Log.w(tag, "Test was interrupted: " + e.getMessage()); return;}
    }

    public static List<String> getItemsString(){
        List<Items> items = Player.get().getItems();
        List<String> itemsStr = new ArrayList<>();
        for(Items i : items){
            itemsStr.add(i.name());
        }
        return itemsStr;
    }

    public static List<Items> getItemsFromString(List<String> itemsStr) {
        List<Items> items = new ArrayList<>();
        for(String s : itemsStr) {
            items.add(Items.valueOf(s));
        }
        return items;
    }

    /*
    Conversion methods for player course list.
    Used for passing data back and forth with Firebase.
     */
    public static List<Course> getCourseListFromStringList(List<String> courseStrings) {
        List<Course> courses = new ArrayList<>();
        for (String courseString : courseStrings) {
            courses.add(Course.valueOf(courseString));
        }
        return courses;
    }
    public static List<String> getStringListFromCourseList(List<Course> courseList) {
        List<String> courseStrings = new ArrayList<>();
        for (Course course : courseList) {
            courseStrings.add(course.name());
        }
        return courseStrings;
    }

    public static Object getOrDefault(Map<String, Object> map, String key, Object defaultRet) {

        if (map.containsKey(key)) {
            return map.get(key);
        }
        else{
            return defaultRet;
        }
    }

    /**
     * Set the locale and put it in the Preferences
     * @param lang A string corresponding to a given language
     */
    public static void setLocale(String lang, Activity act) {
        Locale myLocale = new Locale(lang);
        Resources res = act.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}

