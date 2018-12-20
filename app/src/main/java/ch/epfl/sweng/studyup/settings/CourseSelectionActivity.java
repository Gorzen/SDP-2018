package ch.epfl.sweng.studyup.settings;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.RefreshContext;

import static ch.epfl.sweng.studyup.utils.Constants.COLOR_SETTINGS_KEYWORD;
import static ch.epfl.sweng.studyup.utils.Constants.Course;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_DARK;
import static ch.epfl.sweng.studyup.utils.Constants.SETTINGS_COLOR_RED;
import static ch.epfl.sweng.studyup.utils.Constants.USER_PREFS;

public class CourseSelectionActivity extends RefreshContext {
    final List<CheckBox> courseSelections = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_selection);

        LinearLayout formContainer = findViewById(R.id.courseFormContainer);
        for (Course course : Course.values()) {
            if(course == Course.FakeCourse) continue;

            CheckBox courseCheckbox = new CheckBox(this);
            courseCheckbox.setText(course.name());

            String col = getSharedPreferences(USER_PREFS, MODE_PRIVATE)
                    .getString(COLOR_SETTINGS_KEYWORD, SETTINGS_COLOR_RED);
            int textColor = col.equals(SETTINGS_COLOR_DARK) ?
                    getResources().getColor(R.color.colorGreyClair) :
                    getResources().getColor(R.color.colorDarkkkk);
            courseCheckbox.setTextColor(textColor);
            Player p = Player.get();
            boolean courseSelected = p.getCoursesEnrolled().contains(course);
            if (courseSelected) {
                courseCheckbox.setChecked(true);
            }
            courseSelections.add(courseCheckbox);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 10;

            formContainer.addView(courseCheckbox, params);
        }
    }

    public void updatePlayerCourses(View v) {

        List<Course> updateCourseList = new ArrayList<>();

        for (CheckBox courseCheckbox : courseSelections) {
            if (courseCheckbox.isChecked()) {
                updateCourseList.add(Course.valueOf((String) courseCheckbox.getText()));
            }
        }

        Player.get().setCourses(updateCourseList);
        Toast.makeText(getApplicationContext(), getString(R.string.text_courses_updated), Toast.LENGTH_SHORT).show();

        finish();
    }

    public void backToSettings(View v) {
        finish();
    }
}
