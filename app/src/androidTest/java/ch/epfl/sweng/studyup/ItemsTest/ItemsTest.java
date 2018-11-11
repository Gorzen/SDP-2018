package ch.epfl.sweng.studyup.ItemsTest;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.Items;

public class ItemsTest {

    @Test
    public void getItemDescription() {
        assertEquals(Items.XP_POTION_DESCRIPTION, (Items.XP_POTION).getDescription());
        assertEquals(Items.COIN_SACK_DESCRIPTION, (Items.COIN_SACK).getDescription());
    }

    @Test
    public void getItemName() {
        assertEquals(Items.XP_POTION_NAME, (Items.XP_POTION).getName());
        assertEquals(Items.COIN_SACK_NAME, (Items.COIN_SACK).getName());
    }

    @Test
    public void getItemsFromLong() {
        assertEquals(Items.XP_POTION, Items.getItems(0L));
        assertEquals(Items.COIN_SACK, Items.getItems(1L));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getItemsFromLongThrowsException() throws IllegalArgumentException {
        Items.getItems(-1L);
    }

    @Test
    public void getItemsFromName() {
        assertEquals(Items.XP_POTION, Items.getItems(Items.XP_POTION_NAME));
        assertEquals(Items.COIN_SACK, Items.getItems(Items.COIN_SACK_NAME));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getItemsFromNameThrowsException() throws IllegalArgumentException {
        Items.getItems("La fondue moitié-moitié est indétronable");
    }

    @Test
    public void valueOfReturnsCorrectValue() {
        assertEquals(0, Items.XP_POTION.valueOf());
        assertEquals(1, Items.COIN_SACK.valueOf());
    }

    @Test
    public void getImageName() {
        assertEquals(R.drawable.potion, Items.XP_POTION.getImageName());
        assertEquals(R.drawable.coin_sack, Items.COIN_SACK.getImageName());
    }
}
