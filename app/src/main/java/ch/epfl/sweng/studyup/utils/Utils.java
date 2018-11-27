package ch.epfl.sweng.studyup.utils;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuest;
import ch.epfl.sweng.studyup.specialQuest.SpecialQuestType;

import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIALQUEST_TYPE;
import static ch.epfl.sweng.studyup.utils.Constants.FB_SPECIAL_QUEST_COMPLETION_COUNT;

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
}

