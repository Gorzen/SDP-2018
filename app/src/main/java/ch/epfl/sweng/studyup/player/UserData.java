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
    private HashMap<String, List<String>> answeredQuestions;
    private List<Course> courses;
    private int xp;

    public  UserData(String sciperNum,
                     String firstName,
                     String lastName,
                     HashMap<String, List<String>> answeredQuestions,
                     List<Course> courses,
                     int xp) {
        this.sciperNum=sciperNum;
        this.firstName=firstName;
        this.lastName=lastName;
        this.answeredQuestions=answeredQuestions;
        this.courses=courses;
        this.xp = xp;
    }

    public void setSciperNum(String sciperNum) { this.sciperNum = sciperNum; }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setCourses(List<Course> courses) { this.courses = courses; }
    public void setAnsweredQuestions(HashMap<String, List<String>> answeredQuestions) {
        this.answeredQuestions = answeredQuestions;
    }
    public void setXp(int xp) { this.xp = xp; }

    public List<Course> getCourses() {
        return courses;
    }

    public HashMap<String, List<String>> getAnsweredQuestions() {
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

    public int getXP() { return xp; }
}
