package ch.epfl.sweng.studyup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import ch.epfl.sweng.studyup.course.Course;

@RunWith(RobolectricTestRunner.class)
public class CourseTest {

    @Test
    public void testConstructor() {

        Course testCourse = new Course("123", "SWENG");

        assert(testCourse.getId().equals("123"));
        assert(testCourse.getName().equals("SWENG"));
    }

    @Test
    public void testSetters() {

        Course testCourse = new Course("123", "SWENG");
        testCourse.setId("456");
        testCourse.setName("Underwater Basket Weaving");

        assert(testCourse.getId().equals("456"));
        assert(testCourse.getName().equals("Underwater Basket Weaving"));
    }
}
