package ch.epfl.sweng.studyup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.utils.ListItemAdapter;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ListItemAdapterTest {
   ListItemAdapter listItemAdapter;
   ArrayList<Items> items;

    @Before
    public void init() {
        items = new ArrayList<>(Arrays.asList(Items.XP_POTION, Items.COIN_SACK, Items.XP_POTION));
        listItemAdapter = new ListItemAdapter(null, items, false);
    }

    @Test
    public void getCountReturnsCorrectNumber() {
        assertEquals(items.size(), listItemAdapter.getCount());
    }

    @Test
    public void getItemReturnsCorrectObject() {
        assertEquals(Items.COIN_SACK, listItemAdapter.getItem(1));
    }

    @Test
    public void getItemIdReturnsCorrectId() {
        assertEquals(Items.XP_POTION.ordinal(), listItemAdapter.getItemId(2));
    }
}
