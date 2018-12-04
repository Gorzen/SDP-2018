package ch.epfl.sweng.studyup.npc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.Constants;
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
            currentActivity = GlobalAccessVariables.MOST_RECENT_ACTIVITY;
            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
            builder.setTitle(getName() + currentActivity.getString(R.string.NPC_interaction))
                    .setPositiveButton(currentActivity.getString(R.string.NPC_accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            currentActivity.startActivity(new Intent(currentActivity, NPCActivity.class).putExtra(Constants.NPC_ACTIVITY_INTENT_NAME, name));
                        }
                    })
                    .setNegativeButton(currentActivity.getString(R.string.NPC_refuse), null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public LatLng getPosition() {
        return npcLatLng;
    }
}
