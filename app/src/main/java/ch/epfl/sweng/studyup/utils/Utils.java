package ch.epfl.sweng.studyup.utils;

import android.app.Activity;
import android.util.Log;

import com.google.common.collect.Sets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.Question;

import static ch.epfl.sweng.studyup.firebase.Firestore.userData;

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

    /**
     * Put the given information in Firestore.userData
     */
    public static void putUserData(String key, Object value) {
        if (userData == null) {
            userData = new HashMap<>();
        }

        userData.put(key, value);
    }

    public static void killApp(String tag){
        try{
            Runtime.getRuntime().exec("adb shell pm clear ch.epfl.sweng.studyup\n");
        }catch (IOException e) {
            Log.d(tag, e.getMessage());
        }
    }

    public static List<Integer> getItemsInt(){
        List<Items> items = Player.get().getItems();
        List<Integer> itemsInt = new ArrayList<>();
        for(Items i : items){
            itemsInt.add(i.valueOf());
        }
        return itemsInt;
    }

    public static List<Items> getItemsFromInt(List<Long> itemsInt){
        List<Items> items = new ArrayList<>();
        for(int i = 0; i < itemsInt.size(); ++i){
            items.add(Items.getItems(itemsInt.get(i)));
        }
        return items;
    }

    public static Object getOrDefault(String key, Object defaultRet){
        if(userData.containsKey(key)){
            return userData.get(key);
        }else{
            return defaultRet;
        }
    }
}

