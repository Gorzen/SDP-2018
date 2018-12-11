package ch.epfl.sweng.studyup.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.widget.Toolbar;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.alamkanak.weekview.DateTimeInterpreter;
import com.alamkanak.weekview.MonthLoader;
import com.alamkanak.weekview.WeekView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.npc.NPC;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuest;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestType;

import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIALQUEST_TYPE;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIAL_QUEST_COMPLETION_COUNT;
import static ch.epfl.sweng.studyup.utils.Constants.FIRST_DAY_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.Constants.LAST_DAY_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.Constants.MONTH_OF_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_BLUE;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_BROWN;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_GREEN;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_DARK;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_RED;
import static ch.epfl.sweng.studyup.utils.Constants.YEAR_OF_SCHEDULE;
import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.WEEK_VIEW_LOADER;

public abstract class Utils {

    /**
     * Wait for a given action to be completed. Return an error message if any.
     *
     * @param time time in ms to wait
     * @param tag  error message if any
     */
    public static void waitAndTag(int time, String tag) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {Log.w(tag, "Test was interrupted: " + e.getMessage()); return;}
    }

    public static void setupToolbar(RefreshContext act) {
        Toolbar toolbar = act.findViewById(R.id.toolbar);
        act.setSupportActionBar(toolbar);
        act.getSupportActionBar().setTitle(null);
    }

    public static NPC getNPCfromName(String name) {
        for (NPC npc : Constants.allNPCs) {
            if (npc.getName().equals(name)) {
                return npc;
            }
        }
        return null;
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

    public static List<SpecialQuest> getSpecialQuestListFromMapList(List<Map<String, String>> mapList) {
        if (mapList != null) {
            List<SpecialQuest> specialQuestList = new ArrayList<>();
            for (Map<String, String> specialQuestData : mapList) {
                SpecialQuestType specialQuestType =
                        SpecialQuestType.valueOf(specialQuestData.get(FB_SPECIALQUEST_TYPE));
                SpecialQuest currSpecialQuest = new SpecialQuest(specialQuestType);
                currSpecialQuest.setCompletionCount(
                    Integer.valueOf(specialQuestData.get(FB_SPECIAL_QUEST_COMPLETION_COUNT))
                );
                specialQuestList.add(currSpecialQuest);
            }
            return specialQuestList;
        }
        return null;
    }

    public static List<Map<String, String>> getMapListFromSpecialQuestList(List<SpecialQuest> specialQuestList) {
        List<Map<String, String>> mapList = new ArrayList<>();
        for (SpecialQuest specialQuest : specialQuestList) {
            Map<String, String> currSpecialQuestMap = new HashMap<>();
            currSpecialQuestMap.put(FB_SPECIALQUEST_TYPE, specialQuest.getSpecialQuestType().toString());
            currSpecialQuestMap.put(FB_SPECIAL_QUEST_COMPLETION_COUNT, String.valueOf(specialQuest.getCompletionCount()));
            mapList.add(currSpecialQuestMap);
        }

        return mapList;
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

    public static void setupColor(String col) {
        switch(col) {
            case SETTINGS_COLOR_RED:
                GlobalAccessVariables.APP_THEME = R.style.AppTheme;
                return;
            case SETTINGS_COLOR_GREEN:
                GlobalAccessVariables.APP_THEME = R.style.AppThemeGreen;
                return;
            case SETTINGS_COLOR_BROWN:
                GlobalAccessVariables.APP_THEME = R.style.AppThemeBrown;
                return;
            case SETTINGS_COLOR_BLUE:
                GlobalAccessVariables.APP_THEME = R.style.AppThemeBlue;
                return;
            case SETTINGS_COLOR_DARK:
                GlobalAccessVariables.APP_THEME = R.style.AppThemeDark;
                return;
            default:
                GlobalAccessVariables.APP_THEME = R.style.AppTheme;
        }
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

    public static boolean tooRecentAPI() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.O_MR1;
    }

    public static void disableAllNPCsInteraction() {
        GlobalAccessVariables.NPCInteractionState = false;
    }

    public static void enableAllNPCsInteraction() {
        GlobalAccessVariables.NPCInteractionState = true;
    }
}

