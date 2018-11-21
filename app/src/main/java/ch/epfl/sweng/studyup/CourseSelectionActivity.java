package ch.epfl.sweng.studyup;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.utils.RefreshContext;

import static ch.epfl.sweng.studyup.utils.Constants.Course;

public class CourseSelectionActivity extends RefreshContext {

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_selection);

        final List<CheckBox> courseSelections = new ArrayList<>();

        LinearLayout formContainer = findViewById(R.id.courseFormContainer);
        for (Course course : Course.values()) {

            CheckBox courseCheckbox = new CheckBox(this);
            courseCheckbox.setText(course.name());
            if (Player.get().getCourses().contains(course)) {
                courseCheckbox.setChecked(true);
            }
            courseSelections.add(courseCheckbox);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.bottomMargin = 10;

            formContainer.addView(courseCheckbox, params);
        }

        Button saveButton = new Button(this);
        saveButton.setText(R.string.save_value);
        saveButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View view) {
                try {
                    updatePlayerCourses(courseSelections);
                }
                catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to update courses!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        formContainer.addView(saveButton);
    }

    protected void updatePlayerCourses(List<CheckBox> courseSelections) throws ClassNotFoundException {

        List<Course> updateCourseList = new ArrayList<>();

        for (CheckBox courseCheckbox : courseSelections) {
            if (courseCheckbox.isChecked()) {
                updateCourseList.add(Course.valueOf((String) courseCheckbox.getText()));
            }
        }

        Player.get().setCourses(updateCourseList);
        Toast.makeText(getApplicationContext(), "Courses updated!", Toast.LENGTH_SHORT).show();

        finish();
    }
}