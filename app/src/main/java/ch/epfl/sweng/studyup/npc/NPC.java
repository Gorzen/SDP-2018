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
    private String nameEN;
    private String nameFR;
    private LatLng npcLatLng;
    private int image;
    private final double NPC_RANGE = 20.0;

    public NPC(String nameEN, String nameFR, LatLng latLng, int image) {
        this.nameEN = nameEN;
        this.nameFR = nameFR;
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
                            GlobalAccessVariables.MOST_RECENT_ACTIVITY.startActivity(new Intent(GlobalAccessVariables.MOST_RECENT_ACTIVITY, NPCActivity.class).putExtra("name", getName()));
                        }
                    })
                    .setNegativeButton("Refuse", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public String getName() {
        if(Locale.getDefault().getLanguage().equals("en")) {
            return nameEN;
        } else {
            return nameFR;
        }
    }

    public int getImage() {
        return image;
    }

}
