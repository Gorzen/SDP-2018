package ch.epfl.sweng.studyup.quests;

public interface QuestObserver {
    void update(QuestObservable o, Object param);
}
