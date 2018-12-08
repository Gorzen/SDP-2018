package ch.epfl.sweng.studyup.npc;

import android.app.Activity;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.items.ShopActivity;

public class NPCItems extends NPC {
    private List<Items> items;

    public NPCItems(List<Items> items, String name, LatLng latLng, int image, List<Integer> messages) {
        super(name, latLng, image, messages);
        items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    @Override
    void onYesButton(Activity activity) {
        activity.startActivity(new Intent(activity, ShopActivity.class));
    }
}
