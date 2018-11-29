package ch.epfl.sweng.studyup.questions;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Objects;

import java.io.Serializable;
import java.util.Date;

import ch.epfl.sweng.studyup.R;

import static ch.epfl.sweng.studyup.utils.GlobalAccessVariables.MOST_RECENT_ACTIVITY;

@Entity
public class Question implements Serializable {

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

    @ColumnInfo(name = "lang")
    private String lang;

    @ColumnInfo(name = "clickedInstant")
    private long clickedInstant;

    @ColumnInfo(name = "duration")
    private long duration;

    /**
     * Class for the question
     * @param questionId   The id for the question to use in database as well as image filename
     * @param title        The title of the question
     * @param trueFalse    If the question is a True/False question or not
     * @param answer The number of the answer, starting at 0 (0 is the first answer)
     * @param lang The question lang
     * This is the default constructor and will set the clickedInstant and duration to 0
     */
    public Question(@NonNull String questionId, String title, boolean trueFalse, int answer,
                    String courseName, String lang) {
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

        // If not a basic language
        if (lang == null) {
            this.lang = "en";
        } else {
            this.lang = lang;
        }

        this.clickedInstant = 0;
        this.duration = 0; //one hour
    }

    /**
     * Class for the question
     * @param questionId   The id for the question to use in database as well as image filename
     * @param title        The title of the question
     * @param trueFalse    If the question is a True/False question or not
     * @param answer The number of the answer, starting at 0 (0 is the first answer)
     * @param lang The question lang
     * @param clickedInstant the Date when the user opened the question
     * @param duration the time the user have to answer the question, if 0 then there is
     *                 no constraint
     */
    @Ignore
    public Question(@NonNull String questionId, String title, boolean trueFalse, int answer,
                    String courseName, String lang, long clickedInstant, long duration) {
        this(questionId, title, trueFalse, answer, courseName, lang);
        this.clickedInstant = clickedInstant;
        this.duration = duration;

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

    public String getLang() {
        return this.lang;
    }

    public long getClickedInstant() {
        return clickedInstant;
    }

    public long getDuration() {
        return duration;
    }

    public void setClickedInstant(long clickedInstant) {
        this.clickedInstant = clickedInstant;
    }

    public void setDuration(long duration) {
        this.duration = duration;
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
        Log.i("err", "here");
        String s = "";
        s += "[" + lang + "] ";
        s += title;
        if (trueFalse) {
            s += MOST_RECENT_ACTIVITY.getString(R.string.text_truefalse);
        }
        else {
            s += MOST_RECENT_ACTIVITY.getString(R.string.text_mcq);
        }
        s += s += MOST_RECENT_ACTIVITY.getString(R.string.text_answernumber + answer);
        return s;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(questionId, title, answer, trueFalse);
    }

}