package ch.epfl.sweng.studyup.map;


import com.google.android.gms.maps.model.LatLng;

public final class Room {
    private final LatLng location;

    public Room(double latitude, double longitude){
        this.location = new LatLng(latitude, longitude);
    }

    public LatLng getLocation(){
        return new LatLng(location.latitude, location.longitude);
    }
}
