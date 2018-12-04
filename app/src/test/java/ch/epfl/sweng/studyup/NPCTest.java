package ch.epfl.sweng.studyup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.studyup.npc.NPC;
import ch.epfl.sweng.studyup.utils.Rooms;
import ch.epfl.sweng.studyup.utils.Utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class NPCTest {
    private final NPC roberto = Utils.getNPCfromName("Roberto");
    private final String robertoName = "Roberto";

    @Test
    public void nameTest() {
        assertTrue(robertoName.equals(roberto.getName()));
    }

    @Test
    public void imageTest() {
        assertEquals(R.drawable.roberto, roberto.getImage());
    }

    @Test
    public void positionTest() {
        assertEquals(Rooms.ROOMS_LOCATIONS.get("CM_1_4").getLocation().latitude, roberto.getPosition().latitude, 0);
        assertEquals(Rooms.ROOMS_LOCATIONS.get("CM_1_4").getLocation().longitude, roberto.getPosition().longitude, 0);
    }
}
