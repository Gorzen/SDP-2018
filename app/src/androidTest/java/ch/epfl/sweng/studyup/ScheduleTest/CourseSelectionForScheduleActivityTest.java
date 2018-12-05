package ch.epfl.sweng.studyup.ScheduleTest;

import android.support.test.rule.ActivityTestRule;

import org.junit.BeforeClass;
import org.junit.Rule;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.teacher.CourseSelectionForScheduleActivity;
import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.Course.FakeCourse;
import static ch.epfl.sweng.studyup.utils.Constants.Role.teacher;

public class CourseSelectionForScheduleActivityTest {
    @Rule
    public final ActivityTestRule<CourseSelectionForScheduleActivity> mActivityRule =
            new ActivityTestRule<>(CourseSelectionForScheduleActivity.class);

    @BeforeClass
    public static void setTeachingCourse() {
        Player.get().setRole(teacher);
        List<Course> teachingCourses = new ArrayList<>();
        teachingCourses.add(FakeCourse);
        Player.get().setCourses(teachingCourses);
    }

}
