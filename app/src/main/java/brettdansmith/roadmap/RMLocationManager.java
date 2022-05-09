package brettdansmith.roadmap;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RMLocationManager implements LocationListener {

    private final Context context;
    private Location location;
    private Location mLastLocation;
    private double speed = 0;
    private List<Runnable> runnables = new ArrayList<Runnable>();
    private double maxSpeed = 0;
    private String address;

    public RMLocationManager(Context context) {
        this.context = context;
    }


    public void update() {

    }

    public Location getLocation() {
        return location;
    }

    public double getSpeed() {
        return speed;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public void onLocationChanged(Location pCurrentLocation) {
        //calcul manually speed
        double speed = 0;
        if (this.mLastLocation != null)
            speed = Math.sqrt(
                    Math.pow(pCurrentLocation.getLongitude() - mLastLocation.getLongitude(), 2)
                            + Math.pow(pCurrentLocation.getLatitude() - mLastLocation.getLatitude(), 2)
            ) / (pCurrentLocation.getTime() - this.mLastLocation.getTime());
        //if there is speed from location
        if (pCurrentLocation.hasSpeed())
            //get location speed
            speed = pCurrentLocation.getSpeed();
        this.mLastLocation = pCurrentLocation;

        updateLocation(pCurrentLocation, speed);
    }

    private void updateLocation(Location location, double _speed) {
        this.location = location;

        double longitude = location.getLongitude();
        double latitude = location.getLatitude();

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        double speed = round((location.getSpeed() * 3.6), 2);

        this.speed = speed;
        this.address = address;

        if (speed > this.maxSpeed)
            this.maxSpeed = speed;

        for (Runnable r : runnables) {
            r.run();
        }
    }

    public void addOnUpdated(Runnable runnable) {
        runnables.add(runnable);
    }

    private double round (double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }
}
