package ch.epfl.sweng.studyup;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerUnitTest {
    @Test
    public void resetPlayer() {
        Player.get().reset();

        assertEquals(0, Player.get().getExperience());
        assertEquals(0, Player.get().getLevel());
        assertEquals(0, Player.get().getCurrency());
    }

    @Test
    public void addXpPlayerAndCurrencyTest(){
        Player.get().reset();
        assertEquals(0, Player.get().getExperience());
        assertEquals(0, Player.get().getLevel());
        assertEquals(0, Player.get().getCurrency());

        assertEquals(0.0, Player.get().getLevelProgress(), 10e-6);

        Player.get().addExperience(Player.XP_TO_LEVEL_UP/2);
        assertEquals(0, Player.get().getLevel());
        assertEquals(Player.XP_TO_LEVEL_UP/2, Player.get().getExperience());
        assertEquals(0.5, Player.get().getLevelProgress(), 10e-2);

        Player.get().addExperience(Player.XP_TO_LEVEL_UP);
        assertEquals(1, Player.get().getLevel());
        assertEquals(Player.CURRENCY_PER_LEVEL, Player.get().getCurrency());
        assertEquals((Player.XP_TO_LEVEL_UP * 3) / 2, Player.get().getExperience());
        assertEquals(0.5, Player.get().getLevelProgress(), 10e-2);

        Player.get().addCurrency(100);
        assertEquals(Player.CURRENCY_PER_LEVEL + 100, Player.get().getCurrency());
    }
}
