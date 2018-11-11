package ch.epfl.sweng.studyup.ItemsTest;

import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import ch.epfl.sweng.studyup.items.DisplayItemActivity;

public class DisplayItemActivityTest {
    @Rule
    public final ActivityTestRule<DisplayItemActivity> mActivityRule =
            new ActivityTestRule<>(DisplayItemActivity.class);


}
