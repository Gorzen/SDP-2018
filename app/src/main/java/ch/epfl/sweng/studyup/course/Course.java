package ch.epfl.sweng.studyup.course;

import java.util.UUID;

public class Course {

    public Course(String courseId, String courseName) {
        this.setId(courseId);
        this.setName(courseName);
    }

    private String courseId;
    private String courseName;

    public void setId(String id) {
        this.courseId = id;
    }
    public void setName(String name) {
        this.courseName = name;
    }

    public String getId() { return this.courseId; };
    public String getName() { return this.courseName; };
}
