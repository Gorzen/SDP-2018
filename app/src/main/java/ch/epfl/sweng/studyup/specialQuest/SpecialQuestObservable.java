package ch.epfl.sweng.studyup.specialQuest;

public interface SpecialQuestObservable {
    void addObserver(SpecialQuestObserver o);

    void removeObserver(SpecialQuestObserver o );

    void notifyObservers(Object param);
}
