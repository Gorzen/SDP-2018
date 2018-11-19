package ch.epfl.sweng.studyup.questions;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import com.google.common.base.Objects;

import static ch.epfl.sweng.studyup.utils.Constants.*;

@Entity
public class Question {

    @PrimaryKey
    @NonNull
    private String questionId;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "isTrueFalse")
    private boolean trueFalse;

    @ColumnInfo(name = "answer")
    private int answer;

    @ColumnInfo(name = "courseName")
    private String courseName;

    /**
     * Class for the question
     *
     * @param questionId   The id for the question to use in database as well as image filename
     * @param title        The title of the question
     * @param trueFalse    If the question is a True/False question or not
     * @param answer The number of the answer, starting at 0 (0 is the first answer)
     */
    public Question(@NonNull String questionId, String title, boolean trueFalse, int answer, String courseName) {
        if (answer < 0 || answer > 3 || title == null) {
            throw new IllegalArgumentException();
        }
        if (trueFalse && answer > 1) {
            throw new IllegalArgumentException();
        }
        this.questionId = questionId;
        this.title = title;
        this.trueFalse = trueFalse;
        this.answer = answer;
        this.courseName = courseName;
    }

    public String getCourseName() { return courseName; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        if (trueFalse && answer < 2 && answer >= 0)
            this.answer = answer;
        else if (!trueFalse && answer >= 0 && answer < 4)
            this.answer = answer;
    }

    public boolean isTrueFalse() {
        return trueFalse;
    }

    public void setTrueFalse(boolean trueFalse) {
        this.trueFalse = trueFalse;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        if (questionId != null)
            this.questionId = questionId;
    }


    @Override
    public boolean equals(Object other) {
        if ((other == null) || (getClass() != other.getClass())) {
            return false;
        } else {
            Question o = (Question) other;
            return o.getAnswer() == answer && o.trueFalse == trueFalse && o.title.equals(title);
        }
    }

    @Override
    public String toString() {
        String s = "";
        s += title;
        if (trueFalse) {
            s += " (True/False)";
        }
        else {
            s += " (MCQ)";
        }
        s += " and answer number " + answer;
        return s;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(questionId, title, answer, trueFalse);
    }

}