package ch.epfl.sweng.studyup.npc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.gms.maps.model.LatLng;

import java.util.Locale;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
import ch.epfl.sweng.studyup.utils.Rooms;

public class NPC {
    private String name;
    private LatLng npcLatLng;
    private int image;
    private final double NPC_RANGE = 20.0;

    public NPC(String name, LatLng latLng, int image) {
        this.name = name;
        npcLatLng = latLng;
        this.image = image;
    }

    public void isInRange(LatLng playerLatLng) {
        if (Rooms.distanceBetweenTwoLatLng(npcLatLng, playerLatLng) < NPC_RANGE) {
            AlertDialog.Builder builder = new AlertDialog.Builder(GlobalAccessVariables.MOST_RECENT_ACTIVITY);
            builder.setTitle(getName() + GlobalAccessVariables.MOST_RECENT_ACTIVITY.getString(R.string.NPC_interaction))
                    .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            GlobalAccessVariables.MOST_RECENT_ACTIVITY.startActivity(new Intent(GlobalAccessVariables.MOST_RECENT_ACTIVITY, NPCActivity.class).putExtra("name", name));
                        }
                    })
                    .setNegativeButton("Refuse", null);
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

}
