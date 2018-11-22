package ch.epfl.sweng.studyup.quests;

import java.util.Observable;
import java.util.Random;

import ch.epfl.sweng.studyup.items.Items;
import ch.epfl.sweng.studyup.player.Player;
import ch.epfl.sweng.studyup.questions.Question;

public class QuestionQuestSimple implements SpecialQuest {

    private Items reward;
    private int questionCount;
    private int goal;
    private String title;
    private String description;

    /**
     * Create a new Quest that is acheived when answering a certain number of questions
     * @param title the title of the special quest
     * @param goal the number of question to be answered before the quest is completed
     * @param description a description of the special quest
     */
    public QuestionQuestSimple(String title, String description, int goal) {

        this.title = title;
        this.description = description;

        Random random = new Random();
        reward = Items.values()[random.nextInt(Items.values().length)];

        this.goal = goal;
        this.questionCount = 0;
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
    public double getProgress() { return (double)(questionCount/goal); }

    @Override
    public void onComplete() {
        Player.get().addItem(reward);
    }

    @Override
    public void update(QuestObservable o, Object param) {
        if (param instanceof Question) {
            questionCount++;
            if (questionCount > goal) {
                onComplete();
            }
        }
    }
}
