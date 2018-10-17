package ch.epfl.sweng.studyup.questions;

import android.net.Uri;

public class Question {

    private Uri question;

    private boolean trueFalse;

    private int answer;
    /**
     * Class for the question
     * @param image The Uri of the image with the question to be displayed
     * @param trueFalse If the question is a True/False question or not
     * @param answerNumber The number of the answer, starting at 0 (0 is the first answer)
     */
    public Question(Uri image, boolean trueFalse, int answerNumber) {
        if (answerNumber < 0 || answerNumber > 3 || image == null){
            throw new IllegalArgumentException();
        }
        if (trueFalse && answerNumber > 1) {
            throw new IllegalArgumentException();
        }
        this.trueFalse = trueFalse;
        question = image;
        answer = answerNumber;
    }

    public Uri getQuestionUri() {
        return question;
    }

    public int getAnswer() {
        return answer;
    }

    public boolean isTrueFalseQuestion() {
        return trueFalse;
    }

    @Override
    public boolean equals(Object other){
        if((other == null) || (getClass() != other.getClass())){
            return false;
        }else{
            Question o = (Question)other;
            return o.getAnswer() == answer && o.trueFalse == trueFalse && o.question.equals(question);
        }
    }

    @Override
    public String toString(){
        String s = "";
        if(trueFalse)
            s += "True/False question";
        else
            s += "MCQ question";
        s += " with image " + question;
        s += " and answer number " + answer;
        return s;
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }

}
