package ch.epfl.sweng.studyup.utils.navigation;
import java.util.ArrayList;
import java.util.Arrays;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.questions.AddQuestionActivity;
import ch.epfl.sweng.studyup.teacher.QuestsActivityTeacher;
import ch.epfl.sweng.studyup.utils.Utils;

public class NavigationTeacher extends Navigation {
    private final ArrayList<Integer> buttonIdsTeacher = new ArrayList<>(Arrays.asList(
            R.id.navigation_add_question,
            R.id.navigation_quests_teacher));

    private final ArrayList<Class> activitiesTeacher = new ArrayList<Class>(Arrays.asList(
            AddQuestionActivity.class,
            QuestsActivityTeacher.class));

    private final ArrayList<Integer> activitiesIdTeacher = new ArrayList<>(Arrays.asList(
            Utils.ADD_QUESTION_INDEX,
            Utils.QUESTS_INDEX_TEACHER));

    @Override
    protected void setupNavigation() {
        buttonIds = buttonIdsTeacher;
        activities = activitiesTeacher;
        activitiesId = activitiesIdTeacher;
    }
}