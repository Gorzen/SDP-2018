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

        player.addExperience(Player.XP_TO_LEVEL_UP/2);
        assertEquals(player.getLevel(), 0);
        assertEquals(player.getExperience(), Player.XP_TO_LEVEL_UP/2);
        assertEquals(player.getLevelProgress(), 0.5, 10e-2);

        player.addExperience(Player.XP_TO_LEVEL_UP);
        assertEquals(player.getLevel(), 1);
        assertEquals(player.getExperience(), (Player.XP_TO_LEVEL_UP * 3) / 2);
        assertEquals(player.getLevelProgress(), 0.5, 10e-2);
    }
}
