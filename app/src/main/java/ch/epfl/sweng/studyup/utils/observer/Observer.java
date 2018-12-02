package ch.epfl.sweng.studyup.utils.observer;

import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sweng.studyup.npc.Subject;

public abstract class Observer {
    private Subject subject;
    public abstract void update(LatLng latLng);
}
