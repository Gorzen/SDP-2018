package ch.epfl.sweng.studyup.utils;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;

public class Utils {

    /**
     * Wait for a given action to be completed. Return an error message if any.
     *
     * @param time time in ms to wait
     * @param tag  error message if any
     */
    public static final void waitAndTag(int time, String tag) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            Log.w(tag, "Test was interrupted: " + e.getMessage());
            return;
        }
    }

    public static void killApp(String tag){
        try{
            Runtime.getRuntime().exec("adb shell pm clear ch.epfl.sweng.studyup\n");
        }catch (IOException e) {
            Log.d(tag, e.getMessage());
        }
    }

    public static List<String> getItemsString(){
        List<Items> items = Player.get().getItems();
        List<String> itemsStr = new ArrayList<>();
        for(Items i : items){
            itemsStr.add(i.name());
        }
        return itemsStr;
    }

    public static List<Items> getItemsFromString(List<String> itemsStr){
        List<Items> items = new ArrayList<>();
        for(String s : itemsStr){
            items.add(Items.valueOf(s));
        }
        return items;
    }

    public static Object getOrDefault(Map<String, Object> map, String key, Object defaultRet) {

        if (map.containsKey(key)) {
            return map.get(key);
        }
        else{
            return defaultRet;
        }
    }
}

