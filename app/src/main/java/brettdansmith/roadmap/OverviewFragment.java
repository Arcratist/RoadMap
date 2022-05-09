package brettdansmith.roadmap;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class OverviewFragment extends Fragment implements OnMapReadyCallback {


    private PointerSpeedometer pointerSpeedometer;
    private GoogleMap googleMap;
    private SupportMapFragment googleMapFragment;
    private TextView textMaxSpeed;
    private TextView textCurrentLocation;

    public OverviewFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pointerSpeedometer = (PointerSpeedometer) getActivity().findViewById(R.id.pointerSpeedometer);
        pointerSpeedometer.setWithTremble(false);

        googleMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);
        if (googleMapFragment != null) {
            googleMapFragment.getMapAsync(this);
        }

        textMaxSpeed = (TextView) getActivity().findViewById(R.id.textMaxSpeed);
        textCurrentLocation = (TextView) getActivity().findViewById(R.id.textCurrentLocation);

        MainActivity.locationManager.addOnUpdated(new Runnable () {
            private Marker locationMaker;

            @Override
            public void run() {
                RMLocationManager locationManager = MainActivity.locationManager;
                pointerSpeedometer.speedTo((float)locationManager.getSpeed(), 250);
                Location location = locationManager.getLocation();

                if (googleMapFragment != null && googleMap != null) {
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    if (locationMaker == null) {
                        locationMaker = googleMap.addMarker(new MarkerOptions().position(loc).title("Your Location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                    }else {
                        locationMaker.setPosition(loc);
                    }
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                }

                if (textMaxSpeed != null) {
                    textMaxSpeed.setText("Max Speed: " + locationManager.getMaxSpeed() + "Km/h ");
                }

                if (textCurrentLocation != null) {
                    textCurrentLocation.setText(locationManager.getAddress());
                }

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }
}