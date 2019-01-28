package morris.com.voucher.location;

import android.app.ActivityManager;
import android.content.Context;
import android.location.LocationManager;

/**
 * Created by morris on 22/12/18.
 */
public class LocationSettings {

    private LocationManager locationManager;
    private Context mContext;


    public LocationSettings(Context mContext) {
        this.mContext = mContext;
    }

    public boolean checkGPS() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean checkNetwork() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public boolean checkGPSAndNetwork() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (checkGPS() && checkNetwork()) {
            return true;
        } else
            return false;
    }

    public boolean checkGPSOrNetwork() {
        locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (checkGPS() || checkNetwork()) {
            return true;
        } else
            return false;
    }

    public Boolean locationServiceRunning() {

        ActivityManager manager = (ActivityManager) mContext.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (LocationTracker.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}