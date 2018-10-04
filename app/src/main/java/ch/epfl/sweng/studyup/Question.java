package ch.epfl.sweng.studyup;

import android.widget.ImageView;

public class Question {

    private ImageView question;

    boolean[] answers;
    public Question(ImageView image, boolean trueFalse, int answerNumber) {
        if (answerNumber < 0 || answerNumber > 4 || image == null){
            throw new IllegalArgumentException();
        }
        question = image;
        if (trueFalse) {
            if (answerNumber > 1) throw new IllegalArgumentException();
            answers = new boolean[2];
            answers[answerNumber] = true;
        } else {
            answers = new boolean[4];
            answers[answerNumber] = true;
        }
    }

    public Question(ImageView image, boolean[] answers) {
        if (image == null || answers.length != 2 && answers.length != 4){
            throw new IllegalArgumentException();
        }
        question = image;
        answers = answers;
    }

    public ImageView getQuestion() {
        return question;
    }
}
