package ch.epfl.sweng.studyup.questions;

public class Question {

    private String questionId;

    private String title;

    private boolean trueFalse;

    private int answer;

    /**
     * Class for the question
     *
     * @param questionId   The id for the question to use in database as well as image filename
     * @param title        The title of the question
     * @param trueFalse    If the question is a True/False question or not
     * @param answerNumber The number of the answer, starting at 0 (0 is the first answer)
     */
    public Question(String questionId, String title, boolean trueFalse, int answerNumber) {
        if (answerNumber < 0 || answerNumber > 3) {
            throw new IllegalArgumentException();
        }
        if (trueFalse && answerNumber > 1) {
            throw new IllegalArgumentException();
        }
        this.questionId = questionId;
        this.title = title;
        this.trueFalse = trueFalse;
        answer = answerNumber;
    }

    public String getTitle() { return title; }

    public int getAnswer() {
        return answer;
    }

    public boolean isTrueFalseQuestion() {
        return trueFalse;
    }

    @Override
    public boolean equals(Object other) {
        if ((other == null) || (getClass() != other.getClass())) {
            return false;
        } else {
            Question o = (Question) other;
            return o.getAnswer() == answer && o.trueFalse == trueFalse && o.question.equals(question);
        }
    }

    @Override
    public String toString() {
        String s = "";
        if (trueFalse)
            s += "True/False question";
        else
            s += "MCQ question";
        s += " with image " + question;
        s += " and answer number " + answer;
        return s;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

}
