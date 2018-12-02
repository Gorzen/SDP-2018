package ch.epfl.sweng.studyup.npc;

import android.app.Activity;
import android.app.AlertDialog;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;

import ch.epfl.sweng.studyup.utils.Rooms;

public class NPC {
    private String name;
    private LatLng npcLatLng;

    public NPC(String name, LatLng latLng) {
        this.name = name;
        npcLatLng = latLng;
    }

    public void isInRange(LatLng playerLatLng, WeakReference<Activity> activity) {
        if (Rooms.distanceBetweenTwoLatLng(npcLatLng, playerLatLng) < 30.0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity.get());
            AlertDialog dialog = builder.create();
            dialog.setTitle("hello");
            dialog.show();
        }
    }

}
