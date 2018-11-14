package ch.epfl.sweng.studyup;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;

import static ch.epfl.sweng.studyup.utils.Constants.CURRENCY_PER_LEVEL;
import static ch.epfl.sweng.studyup.utils.Constants.Role;
import static ch.epfl.sweng.studyup.utils.Constants.XP_STEP;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PlayerTest {
    @Before
    public void setup() {
        Player.get().reset();
    }

    @After
    public void cleanup() {
        Player.get().reset();
    }

    @Test
    public void resetTest() {
        Player.get().reset();
        Player.get().setRole(Role.student);
        Player.get().reset();
    }

    @Test
    public void addItemTest() {
        Player.get().addItem(Items.XP_POTION);
        assertEquals(Player.get().getItems().get(0), Items.XP_POTION);
    }

    @Test
    public void consumeItemXpTest() throws Exception {
        Player.get().addItem(Items.XP_POTION);
        int xp = Player.get().getExperience();
        Player.get().consumeItem(Items.XP_POTION);
        assertEquals(xp + XP_STEP, Player.get().getExperience());
    }

    @Test
    public void consumeItemCurrencyTest() throws Exception {
        Player.get().addItem(Items.COIN_SACK);
        int currency = Player.get().getCurrency();
        Player.get().consumeItem(Items.COIN_SACK);
        assertEquals(currency + CURRENCY_PER_LEVEL, Player.get().getCurrency());
    }

    @Test(expected = IllegalArgumentException.class)
    public void consumeNonExistentItemTest() throws Exception {
        Player.get().consumeItem(Items.XP_POTION);
    }
}
