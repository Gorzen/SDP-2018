package ch.epfl.sweng.studyup.utils.navigation;

import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;

import static ch.epfl.sweng.studyup.utils.Constants.ADD_QUESTION_INDEX;
import static ch.epfl.sweng.studyup.utils.Constants.QUESTS_INDEX_TEACHER;

public class NavigationTeacher extends Navigation {
    private final ArrayList<Integer> buttonIdsTeacher = new ArrayList<>(Arrays.asList(
            R.id.navigation_quests_teacher,
            R.id.navigation_add_question));

    private final ArrayList<Class> activitiesTeacher = new ArrayList<Class>(Arrays.asList(
            QuestsActivityTeacher.class,
            AddQuestionActivity.class));

    private final ArrayList<Integer> activitiesIdTeacher = new ArrayList<>(Arrays.asList(
            QUESTS_INDEX_TEACHER,
            ADD_QUESTION_INDEX));

    @Override
    protected void setupNavigation() {
        buttonIds = buttonIdsTeacher;
        activities = activitiesTeacher;
        activitiesId = activitiesIdTeacher;
    }
}
