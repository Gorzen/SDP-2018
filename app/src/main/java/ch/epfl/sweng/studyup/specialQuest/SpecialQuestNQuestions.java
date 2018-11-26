package ch.epfl.sweng.studyup.specialQuest;

import java.util.Random;

import ch.epfl.sweng.studyup.firebase.Firestore;
import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.Question;
import ch.epfl.sweng.studyup.utils.Constants;

public class SpecialQuestNQuestions implements SpecialQuest {

    private final int level;
    private final Items reward;
    private int questionCount;
    private int goal;
    private final String title;
    private final String description;

    /**
     * Create a new Quest that is acheived when answering a certain number of questions
     * @param title the title of the special quest
     * @param goal the number of question to be answered before the quest is completed
     * @param description a description of the special quest
     */
    public SpecialQuestNQuestions(String title, String description, int goal, int level) {

        this.title = title;
        this.description = description;

        Random random = new Random();
        reward = Items.values()[random.nextInt(Items.values().length)];

        this.goal = goal;
        this.questionCount = 0;
        this.level = level;

        //register to the player as an Observer
        if (level < Player.get().getLevel()) {
            Player.get().addObserver(this);
        }
    }

    @Override
    public Items reward() {
        return reward;
    }

    @Override
    public String getTitle() { return title; }

    @Override
    public String getDescription() { return description; }

    @Override
    public double getProgress() { return (questionCount/(double)goal); }

    @Override
    public int getGoal() {
        return goal;
    }

    @Override
    public Constants.SpecialQuestsType getId() {
        return Constants.SpecialQuestsType.NQUESTIONS;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setGoal(int goal) {
        this.goal = goal;
    }

    @Override
    public void setProgress(double progress) {
        this.questionCount = (int)(Math.round(goal*progress));
    }

    @Override
    public void onComplete() {
        Player currPlayer = Player.get();
        currPlayer.addItem(reward);
        currPlayer.removeObserver(this);
        currPlayer.removeQuest(this);
        Firestore.get().updateRemotePlayerDataFromLocal();
    }

    @Override
    public void update(SpecialQuestObservable o, Object param) {
        if (param instanceof Question) {
            if (questionCount < goal) {
                questionCount++;
                Firestore.get().updateRemotePlayerDataFromLocal();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SpecialQuestNQuestions) {
            SpecialQuestNQuestions other = (SpecialQuestNQuestions) o;
            return other.title == this.title && other.description == this.description;
        } else {
            return false;
        }
    }
}
