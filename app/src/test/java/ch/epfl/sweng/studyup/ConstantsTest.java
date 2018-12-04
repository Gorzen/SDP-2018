package ch.epfl.sweng.studyup;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.epfl.sweng.studyup.utils.Constants;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ConstantsTest {

    @Test
    public void getNamesForCoursesReturnsCorrectListTest() {
        List<Constants.Course> courses = Arrays.asList(Constants.Course.Algebra, Constants.Course.SWENG, Constants.Course.Algebra);
        List<String> coursesNames = Constants.Course.getNamesFromCourses(courses);
        assertEquals(Arrays.asList("Algebra", "SWENG", "Algebra"), coursesNames);
    }

    @Test
    public void getNamesForCoursesReturnsEmptyListTest() {
        List<String> coursesNames = Constants.Course.getNamesFromCourses(new ArrayList<Constants.Course>());
        assertEquals(0, coursesNames.size());
    }
}
