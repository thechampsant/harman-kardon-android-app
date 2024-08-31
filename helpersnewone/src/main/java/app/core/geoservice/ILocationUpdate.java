package app.core.geoservice;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public interface ILocationUpdate {

	public void setUpdateLocationListener(GoogleMap mMap, Marker marker);
}
