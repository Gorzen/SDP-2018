package ch.epfl.sweng.studyup.ItemsTest;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.items.InventoryActivity;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.adapters.ListCourseAdapter;
import ch.epfl.sweng.studyup.utils.adapters.ListItemAdapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ItemsInstrumentationTest {
    private ListItemAdapter listItemAdapter;
    private ArrayList<Items> items;
    private ListCourseAdapter listCourseAdapter;
    private ArrayList<Constants.Course> courses;

    @Rule
    public final ActivityTestRule<InventoryActivity> mActivityRule =
            new ActivityTestRule<>(InventoryActivity.class);

    @Before
    public void init() {
        items = new ArrayList<>(Arrays.asList(Items.XP_POTION, Items.COIN_SACK, Items.XP_POTION));
        courses = new ArrayList<>(Arrays.asList(Constants.Course.SWENG, Constants.Course.Algebra, Constants.Course.Ecology));
        listItemAdapter = new ListItemAdapter(mActivityRule.getActivity(), items, R.id.list_view_shop, false);
        listCourseAdapter = new ListCourseAdapter(mActivityRule.getActivity(), courses, R.layout.course_item_model, true);
    }

    @Test
    public void consumeGreenThemeUnlocksIt() {
        Player.get().resetPlayer();
        Items.GREEN_THEME.consume();
        assertTrue(Player.get().getUnlockedThemes().contains(Constants.SETTINGS_COLOR_GREEN));
        Items.BLUE_THEME.consume();
        assertTrue(Player.get().getUnlockedThemes().contains(Constants.SETTINGS_COLOR_BLUE));
        Items.ORANGE_THEME.consume();
        assertTrue(Player.get().getUnlockedThemes().contains(Constants.SETTINGS_COLOR_ORANGE));
        Items.DARK_THEME.consume();
        assertTrue(Player.get().getUnlockedThemes().contains(Constants.SETTINGS_COLOR_DARK));
    }

    @Test
    public void consumeMapUnlocksAllNPCs() {
        Player.get().resetPlayer();
        Items.MAP.consume();
        assertEquals(Constants.allNPCs.size(), Player.get().getKnownNPCs().size());
    }

    @Test
    public void consumeUnstablePotionGrantsXP() {
        Player.get().resetPlayer();
        int xp = Player.get().getExperience();
        Items.UNSTABLE_POTION.consume();
        assertTrue((xp != Player.get().getExperience()));
    }

    @Test
    public void consumeTombolaTicketGivesMoney() {
        Player.get().resetPlayer();
        int currency = Player.get().getCurrency();
        Items.TOMBOLA.consume();
        assertTrue((currency != Player.get().getCurrency()));
    }

    @Test
    public void getCountReturnsCorrectNumber() {
        assertEquals(items.size(), listItemAdapter.getCount());
        assertEquals(courses.size(), listCourseAdapter.getCount());
    }

    //Beware the list is sorted in ListItemAdapter's constructor
    @Test
    public void getItemReturnsCorrectObject() {
        assertEquals(Items.COIN_SACK, listItemAdapter.getItem(0));
        assertEquals(Constants.Course.Algebra, listCourseAdapter.getItem(0));
    }

    @Test
    public void getItemIdReturnsCorrectId() {
        assertEquals(Items.XP_POTION.ordinal(), listItemAdapter.getItemId(2));
        assertEquals(Constants.Course.SWENG.ordinal(), listItemAdapter.getItemId(2));
    }

    @Test
    public void getItemDescription() {
        assertEquals(mActivityRule.getActivity().getString(Items.XP_POTION_DESCRIPTION_ID), (Items.XP_POTION).getDescription());
        assertEquals(mActivityRule.getActivity().getString(Items.COIN_SACK_DESCRIPTION_ID), (Items.COIN_SACK).getDescription());
    }

    @Test
    public void getItemName() {
        assertEquals(mActivityRule.getActivity().getString(Items.XP_POTION_NAME_ID), (Items.XP_POTION).getName());
        assertEquals(mActivityRule.getActivity().getString(Items.COIN_SACK_NAME_ID), (Items.COIN_SACK).getName());
    }

    @Test
    public void getItemsFromName() {
        assertEquals(Items.XP_POTION, Items.getItemFromName(Items.XP_POTION.getName()));
        assertEquals(Items.COIN_SACK, Items.getItemFromName(Items.COIN_SACK.getName()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getItemsFromNameThrowsException() throws IllegalArgumentException {
        Items.getItemFromName("La fondue moitié-moitié est indétronable");
    }
}
