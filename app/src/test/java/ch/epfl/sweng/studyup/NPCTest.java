package ch.epfl.sweng.studyup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.studyup.npc.NPC;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class NPCTest {
    private final String name = "Coco";
    private final int image = 32913;
    private final NPC npc = new NPC(name, null, image);

    @Test
    public void getNameTest() {
        assertTrue(npc.getName().equals(name));
    }

    @Test
    public void getImageTest() {
        assertEquals(image, npc.getImage());
    }
}
