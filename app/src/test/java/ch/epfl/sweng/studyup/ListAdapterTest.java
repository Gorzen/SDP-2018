package ch.epfl.sweng.studyup;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.adapters.ListCourseAdapter;
import ch.epfl.sweng.studyup.utils.adapters.ListItemAdapter;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ListAdapterTest {
    ListItemAdapter listItemAdapter;
    ArrayList<Items> items;
    ListCourseAdapter listCourseAdapter;
    ArrayList<Constants.Course> courses;



    @Before
    public void init() {
        items = new ArrayList<>(Arrays.asList(Items.XP_POTION, Items.COIN_SACK, Items.XP_POTION));
        courses = new ArrayList<>(Arrays.asList(Constants.Course.SWENG, Constants.Course.Algebra, Constants.Course.Ecology));
        listItemAdapter = new ListItemAdapter(null, items, false);
        listCourseAdapter = new ListCourseAdapter(null, courses);
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
}
