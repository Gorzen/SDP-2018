package ch.epfl.sweng.studyup.utils.navigation;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.teacher.CourseSelectionForScheduleActivity;
import ch.epfl.sweng.studyup.teacher.CourseStatsActivity;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;

import static ch.epfl.sweng.studyup.utils.Constants.QUESTS_INDEX_TEACHER;
import static ch.epfl.sweng.studyup.utils.Constants.COURSE_STAT_INDEX;
import static ch.epfl.sweng.studyup.utils.Constants.COURSE_SELECTION_FOR_SCHEDULE_INDEX;

public class NavigationTeacher extends Navigation {
    private final ArrayList<Integer> buttonIdsTeacher = new ArrayList<>(Arrays.asList(
            R.id.navigation_quests_teacher,
            R.id.navigation_courses_stats_teacher,
            R.id.navigation_courses_schedule_teacher));

    private final ArrayList<Class> activitiesTeacher = new ArrayList<Class>(Arrays.asList(
            QuestsActivityTeacher.class,
            CourseStatsActivity.class,
            CourseSelectionForScheduleActivity.class));

    private final ArrayList<Integer> activitiesIdTeacher = new ArrayList<>(Arrays.asList(
            QUESTS_INDEX_TEACHER,
            COURSE_STAT_INDEX,
            COURSE_SELECTION_FOR_SCHEDULE_INDEX));

    @Override
    protected void setupNavigation() {
        buttonIds = buttonIdsTeacher;
        activities = activitiesTeacher;
        activitiesId = activitiesIdTeacher;
    }
}
