package ch.epfl.sweng.studyup.player;

import java.util.HashMap;
import java.util.List;


import ch.epfl.sweng.studyup.utils.Constants.Course;

/**
 * UserData class is used to compute statistics for each users without
 * using the player class (which we want to be a singleton)
 */
public class UserData {
    private String sciperNum;
    private String firstName;
    private String lastName;
    private HashMap<String, Boolean> answeredQuestions;
    private List<Course> courses;

    public  UserData(String sciperNum,
                     String firstName,
                     String lastName,
                     HashMap<String, Boolean> answeredQuestions,
                     List<Course> courses) {
        this.sciperNum=sciperNum;
        this.firstName=firstName;
        this.lastName=lastName;
        this.answeredQuestions=answeredQuestions;
        this.courses=courses;
    }

    public void setSciperNum(String sciperNum) { this.sciperNum = sciperNum; }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setCourses(List<Course> courses) { this.courses = courses; }
    public void setAnsweredQuestions(HashMap<String, Boolean> answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public HashMap<String, Boolean> getAnsweredQuestions() {
        return answeredQuestions;
    }


    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSciperNum() {
        return sciperNum;
    }
}
