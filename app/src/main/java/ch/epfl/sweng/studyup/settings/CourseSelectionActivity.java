package ch.epfl.sweng.studyup.settings;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.R;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.Constants;
import ch.epfl.sweng.studyup.utils.RefreshContext;

import static ch.epfl.sweng.studyup.utils.Constants.Course;

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

    protected void updatePlayerCourses(View v) {

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
