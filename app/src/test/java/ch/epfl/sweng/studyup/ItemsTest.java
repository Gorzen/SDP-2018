package ch.epfl.sweng.studyup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ch.epfl.sweng.studyup.items.Items;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("HardCodedStringLiteral")
@RunWith(JUnit4.class)
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
    public void getItemsFromName() {
        assertEquals(Items.XP_POTION, Items.getItemFromName(Items.XP_POTION_NAME));
        assertEquals(Items.COIN_SACK, Items.getItemFromName(Items.COIN_SACK_NAME));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getItemsFromNameThrowsException() throws IllegalArgumentException {
        Items.getItemFromName("La fondue moitié-moitié est indétronable");
    }

    @Test
    public void getImageName() {
        assertEquals(R.drawable.potion, Items.XP_POTION.getImageName());
        assertEquals(R.drawable.coin_sack, Items.COIN_SACK.getImageName());
    }
}
