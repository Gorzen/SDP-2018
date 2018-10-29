package ch.epfl.sweng.studyup;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import ch.epfl.sweng.studyup.firebase.FirebaseCloud;

@RunWith(AndroidJUnit4.class)
public class FirebaseCloudTest {
    @Rule
    public final ActivityTestRule<MainActivity> mActivityRule =
            new ActivityTestRule<>(MainActivity.class);
    @Test
    public void testFileUpload() {
    }
}