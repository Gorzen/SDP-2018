package ch.epfl.sweng.studyup.quests;

public interface QuestObservable {
    void addObserver(QuestObserver o);

    void removeObserver(QuestObserver o );

    void notifyObservers(Object param);
}
