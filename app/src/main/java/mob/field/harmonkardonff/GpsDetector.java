package mob.field.harmonkardonff;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;

public class GpsDetector implements LocationListener
{
    Activity main;
    public static boolean isGPSon;
    public GpsDetector(Activity activity)
    {
        main = activity;
    }
    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(main,"onLocationChanged : "+location.getLatitude(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(main,"GPS Enabled",Toast.LENGTH_SHORT).show();
        isGPSon = true;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(main,"GPS Disabled",Toast.LENGTH_SHORT).show();
        isGPSon = false;
    }
}
