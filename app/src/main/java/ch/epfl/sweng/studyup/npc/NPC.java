package ch.epfl.sweng.studyup.npc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.map.MapsActivity;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.GlobalAccessVariables;
import ch.epfl.sweng.studyup.utils.Rooms;

public abstract class NPC {
    private String name;
    private LatLng npcLatLng;
    private int image;
    private final int MAX_COUNTER = 100;
    private boolean isFirstInteraction = true;
    private Activity currentActivity;
    private int counter = 0;
    private List<Integer> messages;

    public NPC(String name, LatLng latLng, int image, List<Integer> messages) {
        this.name = name;
        npcLatLng = latLng;
        this.image = image;
        this.messages = Collections.unmodifiableList(messages);
    }

    public boolean checkNPCInteraction(LatLng playerLatLng) {
        currentActivity = GlobalAccessVariables.MOST_RECENT_ACTIVITY;
        if (!Player.get().getKnownNPCs().contains(name)
                && GlobalAccessVariables.NPCInteractionState
                && !(currentActivity instanceof MapsActivity)
                && Rooms.distanceBetweenTwoLatLng(npcLatLng, playerLatLng) < Constants.NPC_RANGE
                && (counter >= MAX_COUNTER || isFirstInteraction)) {
            counter = 0;
            AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
            builder.setTitle(getName() + " " + currentActivity.getString(R.string.NPC_interaction))
                    .setPositiveButton(currentActivity.getString(R.string.NPC_accept), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            currentActivity.startActivity(new Intent(currentActivity, NPCActivity.class).putExtra(Constants.NPC_ACTIVITY_INTENT_NAME, name));
                        }
                    })
                    .setNegativeButton(currentActivity.getString(R.string.NPC_refuse), null);
            AlertDialog dialog = builder.create();
            dialog.show();
            isFirstInteraction = false;
            Player.get().addKnownNPC(this);

            return true;
        }
        ++counter;
        return false;
    }

    abstract void onYesButton(Activity activity);

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public LatLng getPosition() {
        return npcLatLng;
    }

    public List<Integer> getMessages(){
        return new ArrayList<>(messages);
    }
}
