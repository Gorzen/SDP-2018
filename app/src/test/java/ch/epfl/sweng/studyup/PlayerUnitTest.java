package ch.epfl.sweng.studyup;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerUnitTest {
    @Test
    public void constructorPlayer() {
        Player player = new Player();
        assertEquals(player.getExperience(), 0);
        assertEquals(player.getLevel(), 0);
    }

    @Test
    public void addXpPlayer(){
        Player player = new Player();
        assertEquals(player.getLevelProgress(), 0.0, 10e-6);

        player.addExperience(20);
        assertEquals(player.getLevel(), 0);
        assertEquals(player.getExperience(), 20);
        assertEquals(player.getLevelProgress(), 0.2, 10e-6);

        player.addExperience(100);
        assertEquals(player.getLevel(), 1);
        assertEquals(player.getExperience(), 120);
        assertEquals(player.getLevelProgress(), 0.2, 10e-6);
    }
}
