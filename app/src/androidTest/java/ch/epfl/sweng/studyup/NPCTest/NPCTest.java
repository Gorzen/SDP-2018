package ch.epfl.sweng.studyup.NPCTest;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.npc.NPC;
import ch.epfl.sweng.studyup.player.HomeActivity;
import ch.epfl.sweng.studyup.utils.Rooms;
import ch.epfl.sweng.studyup.utils.Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class NPCTest {
    private final NPC roberto = Utils.getNPCfromName("Roberto");
    private final String robertoName = "Roberto";

    @Rule
    public ActivityTestRule<InventoryActivity> mActivity = new ActivityTestRule<>(InventoryActivity.class);

    @Test
    public void checkIfInteractionWithNPCDisplays() throws Throwable {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roberto.isInRange(roberto.getPosition());
            }
        });
    }

    @Test
    public void nameTest() {
        assertTrue(robertoName.equals(roberto.getName()));
    }

    @Test
    public void imageTest() {
        Assert.assertEquals(R.drawable.roberto, roberto.getImage());
    }

    @Test
    public void positionTest() {
        assertEquals(Rooms.ROOMS_LOCATIONS.get("CM_1_4").getLocation().latitude, roberto.getPosition().latitude, 0);
        assertEquals(Rooms.ROOMS_LOCATIONS.get("CM_1_4").getLocation().longitude, roberto.getPosition().longitude, 0);
    }

    @Test
    public void enableInteractionTest() {
        assertTrue(roberto.enableNPCInteraction());
    }

    @Test
    public void disableInteractionTest() {
        assertFalse(roberto.disableNPCInteraction());
    }

    @Test
    public void getNPCInteractionStateTest() {
        roberto.enableNPCInteraction();
        assertTrue(roberto.getNPCInteractionState());
    }
}
