package ch.epfl.sweng.studyup.ItemsTest;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
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
        Player.get().reset();
    }

    @After
    public void cleanup(){
        Player.get().reset();
    }

    @Test
    public void getItemsIntTest(){
        Player.get().addItem(Items.XP_POTION);
        Player.get().addItem(Items.COIN_SACK);
        List<Integer> items = Utils.getItemsInt();
        assertEquals(0, (int)items.get(0));
        assertEquals(1, (int)items.get(1));
    }

    @Test
    public void getItemsFromIntTest(){
        List<Long> itemsLong = new ArrayList<Long>(){
            {
                add(0l);
                add(1l);
            }
        };
        List<Items> items = Utils.getItemsFromInt(itemsLong);
        assertEquals(items.get(0), Items.XP_POTION);
        assertEquals(items.get(1), Items.COIN_SACK);
    }
}
