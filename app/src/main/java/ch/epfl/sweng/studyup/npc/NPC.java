package ch.epfl.sweng.studyup.npc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.lang.ref.WeakReference;

import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
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
            builder.setTitle("An NPC wants to interfact with you")
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GlobalAccessVariables.MOST_RECENT_ACTIVITY.startActivity(new Intent(GlobalAccessVariables.MOST_RECENT_ACTIVITY, NPCActivity.class));
                        }
                    })
                    .setNegativeButton("Refuse", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

}
