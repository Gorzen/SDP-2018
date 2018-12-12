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

    public NPCItems(List<Items> items, String name, LatLng latLng, int image, int numberOfMessages) {
        super(name, latLng, image, numberOfMessages);
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    @Override
    void onYesButton(Activity activity) {
        String[] namesOfItems = new String[items.size()];

        for(int i = 0; i < items.size(); ++i){
            namesOfItems[i] = items.get(i).name();
        }

        activity.startActivity(new Intent(activity, ShopActivity.class).putExtra(Items.class.getName(), namesOfItems));
    }
}
