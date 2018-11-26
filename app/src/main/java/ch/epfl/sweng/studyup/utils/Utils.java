package ch.epfl.sweng.studyup.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;

import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.FIRST_DAY_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.Constants.LAST_DAY_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.Constants.MONTH_OF_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.Constants.YEAR_OF_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.WEEK_VIEW_LOADER;

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
    public static ArrayList<String> getStringListFromCourseList(List<Course> courseList, boolean niceName) {
        ArrayList<String> courseStrings = new ArrayList<>();
        for(Course course : courseList) {
            if(niceName) {
                courseStrings.add(course.toString());
            }
            else courseStrings.add(course.name());
        }
        return courseStrings;
    }

    public static Object getOrDefault(Map<String, Object> map, String key, Object defaultRet) {

        if (map.containsKey(key) && map.get(key) != null) {
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

    public static void setupWeekView(WeekView weekView,
                                           WeekView.EventLongPressListener eventLongPressListener,
                                           DateTimeInterpreter dateTimeInterpreter,
                                           MonthLoader.MonthChangeListener monthChangeListener,
                                           WeekView.EventClickListener eventClickListener,
                                           WeekView.EmptyViewClickListener emptyViewClickListener) {
        weekView.setEventLongPressListener(eventLongPressListener);
        weekView.setDateTimeInterpreter(dateTimeInterpreter);
        weekView.setWeekViewLoader(WEEK_VIEW_LOADER);
        weekView.setMonthChangeListener(monthChangeListener);

        Utils.setStartAndEndSchedule(weekView);

        weekView.setOnEventClickListener(eventClickListener);
        weekView.setEmptyViewClickListener(emptyViewClickListener);
    }

    private static void setStartAndEndSchedule(WeekView weekView) {
        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, YEAR_OF_SCHEDULE);
        startDate.set(Calendar.MONTH, MONTH_OF_SCHEDULE);
        startDate.set(Calendar.DAY_OF_MONTH, FIRST_DAY_SCHEDULE);
        startDate.set(Calendar.HOUR_OF_DAY, 0);
        startDate.set(Calendar.MINUTE, 0);

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.YEAR, YEAR_OF_SCHEDULE);
        endDate.set(Calendar.MONTH, MONTH_OF_SCHEDULE);
        endDate.set(Calendar.DAY_OF_MONTH, LAST_DAY_SCHEDULE);
        endDate.set(Calendar.HOUR_OF_DAY, 23);
        endDate.set(Calendar.MINUTE, 59);

        weekView.goToDate(startDate);

        weekView.setMinDate(startDate);
        weekView.setMaxDate(endDate);

        weekView.setMinTime(8);
        weekView.setMaxTime(20);
    }
}

