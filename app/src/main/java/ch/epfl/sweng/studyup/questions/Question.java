package ch.epfl.sweng.studyup.questions;

import android.arch.persistence.room.*;
import android.net.Uri;

import com.google.common.base.Objects;

@Entity
public class Question {

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "questionUri")
    private String questionUriString;

    @ColumnInfo(name = "isTrueFalse")
    private boolean isTrueFalse;

    @ColumnInfo(name = "answer")
    private int answer;

    /**
     * Class for the question
     *
     * @param image        The Uri of the image with the question to be displayed
     * @param trueFalse    If the question is a True/False question or not
     * @param answerNumber The number of the answer, starting at 0 (0 is the first answer)
     */
    public Question(Uri image, boolean trueFalse, int answerNumber) {
        if (answerNumber < 0 || answerNumber > 3 || image == null) {
            throw new IllegalArgumentException();
        }
        if (trueFalse && answerNumber > 1) {
            throw new IllegalArgumentException();
        }
        this.isTrueFalse = trueFalse;
        questionUriString = image.toString();
        answer = answerNumber;
    }

    /**
     * Class for the Question
     * @param questionUriString A string representation of the Uri of the image
     * @param isTrueFalse
     * @param answer the number of the answer between 0 and 3 (included)
     */
    public Question(String questionUriString, boolean isTrueFalse, int answer){
        this(Uri.parse(questionUriString), isTrueFalse, answer);
    }

    public Uri getQuestionUri() {
        return Uri.parse(questionUriString);
    }

    public void setQuestionUri(Uri uri) {
        if (uri != null)
        questionUriString = uri.toString();
    }

    public String getQuestionUriString() { return questionUriString;}

    public void setQuestionUriString(String uri){
        if (uri != null)
            questionUriString = uri;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int newAnswer){
        if (answer < 0 || answer > 3) {
            if (isTrueFalse && answer < 2) {
                answer = newAnswer;
             }
        }
    }

    public boolean isTrueFalse() {
        return isTrueFalse;
    }

    public void setTrueFalse(boolean isTrueFalse){ this.isTrueFalse = isTrueFalse; }

    public int getUid(){ return uid; }

    public void setUid(int uid){ this.uid = uid; }

    @Override
    public boolean equals(Object other) {
        if ((other == null) || (getClass() != other.getClass())) {
            return false;
        } else {
            Question o = (Question) other;
            return o.getAnswer() == answer && o.isTrueFalse == isTrueFalse && o.questionUriString.equals(questionUriString);
        }
    }

    @Override
    public String toString() {
        String s = "";
        if (isTrueFalse)
            s += "True/False question";
        else
            s += "MCQ question";
        s += " with image " + questionUriString;
        s += " and answer number " + answer;
        return s;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(answer, questionUriString, isTrueFalse);
    }

}
