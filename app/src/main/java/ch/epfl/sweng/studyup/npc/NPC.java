package ch.epfl.sweng.studyup.npc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
import ch.epfl.sweng.studyup.utils.Rooms;

public class NPC {
    private String name;
    private LatLng npcLatLng;
    private int image;
    private final double NPC_RANGE = 20.0;
    private Activity currentActivity;

    public NPC(String name, LatLng latLng, int image) {
        this.name = name;
        npcLatLng = latLng;
        this.image = image;
    }

    public void isInRange(LatLng playerLatLng) {
        if (Rooms.distanceBetweenTwoLatLng(npcLatLng, playerLatLng) < NPC_RANGE) {
            HomeActivity activity = (HomeActivity)GlobalAccessVariables.MOST_RECENT_ACTIVITY;
            activity.showDialog(name);
        }
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

}
