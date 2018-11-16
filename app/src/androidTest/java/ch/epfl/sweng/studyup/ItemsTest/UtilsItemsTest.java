package ch.epfl.sweng.studyup.ItemsTest;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Utils;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class UtilsItemsTest {
    @Before
    public void setup(){
        Player.resetPlayer();
    }

    @Test
    public void getItemsIntTest(){
        Player.get().addItem(Items.XP_POTION);
        Player.get().addItem(Items.COIN_SACK);
        List<String> items = Utils.getItemsString();
        assertEquals(Items.XP_POTION.name(), items.get(0));
        assertEquals(Items.COIN_SACK.name(), items.get(1));
    }

    @Test
    public void getItemsFromIntTest(){
        List<String> itemsLong = new ArrayList<String>(){
            {
                add(Items.XP_POTION.name());
                add(Items.COIN_SACK.name());
            }
        };
        List<Items> items = Utils.getItemsFromString(itemsLong);
        assertEquals(items.get(0), Items.XP_POTION);
        assertEquals(items.get(1), Items.COIN_SACK);
    }
}
