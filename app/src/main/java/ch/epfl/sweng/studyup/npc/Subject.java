package ch.epfl.sweng.studyup.npc;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.studyup.utils.observer.Observer;

public class Subject {
    private List<Observer> NPCS = new ArrayList<>();

    public void attach(Observer observer) {
        NPCS.add(observer);
    }

    public void notifyAllObservers(LatLng latLng) {
        for (Observer observer : NPCS) {
            observer.update(latLng);
        }
    }
}
