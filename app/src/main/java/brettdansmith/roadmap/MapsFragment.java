package brettdansmith.roadmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsFragment extends Fragment {
    private GoogleMap googleMap;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {



        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap _googleMap) {
            googleMap = _googleMap;
        }
    };
    private SupportMapFragment googleMapFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        googleMapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (googleMapFragment != null) {
            googleMapFragment.getMapAsync(callback);
        }

        MainActivity.locationManager.addOnUpdated(new Runnable () {
            private Marker locationMaker;

            @Override
            public void run() {
                RMLocationManager locationManager = MainActivity.locationManager;
                Location location = locationManager.getLocation();

                if (googleMapFragment != null && googleMap != null) {
                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                    if (locationMaker == null) {
                        locationMaker = googleMap.addMarker(new MarkerOptions().position(loc).title("Your Location"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15));
                    }else {
                        locationMaker.setPosition(loc);
                    }
                   // googleMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
                }

            }
        });
    }
}