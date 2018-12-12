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
    public void getImageName() {
        assertEquals(R.drawable.item_potion, Items.XP_POTION.getImageName());
        assertEquals(R.drawable.coin_sack, Items.COIN_SACK.getImageName());
    }
}
