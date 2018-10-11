package ch.epfl.sweng.studyup;

import com.google.android.gms.maps.model.LatLng;

public interface GetLocationListener {
    void onLocationUpdate(LatLng latLong);
}
