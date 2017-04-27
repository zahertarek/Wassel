package com.guc.wasel;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener,LocationListener{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location myLocation;
    private PlaceAutocompleteFragment placeAutocompleteFragment;
    private ProgressDialog progressDialog;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please Wait");
        progressDialog.setMessage("Loading Point of Intrests");
        progressDialog.show();

    }

    @Override
    public void onLocationChanged(Location location) {
        placeAutocompleteFragment.setBoundsBias(new LatLngBounds(new LatLng(location.getLatitude(),location.getLongitude()),new LatLng(location.getLatitude(),location.getLongitude())));
    }

    @Override
    public void onStart() {
        super.onStart();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) this);

        placeAutocompleteFragment = (PlaceAutocompleteFragment)getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        placeAutocompleteFragment.setBoundsBias(new LatLngBounds(new LatLng(30.044,31.2357),new LatLng(30.044,31.2357)));
        placeAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));

            }

            @Override
            public void onError(Status status) {

            }
        });

    }




    @Override
    public void onResume() {
        super.onResume();
        progressDialog.show();
        loadPois();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_maps, container, false);

    }
    public void onDestroyView() {
        super.onDestroyView();
        PlaceAutocompleteFragment placeAutocompleteFragment = (PlaceAutocompleteFragment)getActivity().getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
        if (placeAutocompleteFragment != null)
            getActivity().getFragmentManager().beginTransaction().remove(placeAutocompleteFragment).commit();
    }

    public void adjustCameraLocation() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        try {
            myLocation = locationManager.getLastKnownLocation(provider);
            double latitude;
            double longitude;
            int zoom;
            if (myLocation != null) {
                // Get latitude of the current location
                latitude = myLocation.getLatitude();

                // Get longitude of the current location
                longitude = myLocation.getLongitude();

                zoom = 15;
            } else {

                latitude = 30.0444;
                longitude = 31.2357;
                zoom = 10;
            }

            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Show the current location in Google Map
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
        } catch (SecurityException e) {
            Log.e("MapsActivity", "Permission Exception");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            adjustCameraLocation();
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 123);

        }


        loadPois();
        mMap.setOnInfoWindowClickListener(this);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            adjustCameraLocation();
        }else
            Toast.makeText(getActivity(), "Please enable location to use our application", Toast.LENGTH_LONG).show();


}





    private void loadPois(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi");
        ref.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Map<String,Object> pois = (Map<String,Object>) dataSnapshot.getValue();
                        if(pois != null)
                        for(Map.Entry<String,Object> poi : pois.entrySet()){
                            Map poiEntry = (Map) poi.getValue();

                            boolean isVerified = (boolean) poiEntry.get("verified");
                            LatLng latLng = new LatLng((Double) poiEntry.get("latitude"),(Double)poiEntry.get("longitude"));

                            if(isVerified) {
                                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).
                                        title((String) poiEntry.get("name")).
                                        snippet("Click Here For More Info").
                                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                                marker.setTag(poi.getKey());
                            }else if( !isVerified && MainActivity.isAdmin() ){
                                Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).
                                        title((String) poiEntry.get("name")).
                                        snippet("Click Here For More Info").
                                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                marker.setTag(poi.getKey());
                            }
                        }
                        progressDialog.hide();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        final String poiId =(String) marker.getTag();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(poiId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map poiEntry = (Map) dataSnapshot.getValue();
                Poi poi = new Poi();
                poi.setName((String)poiEntry.get("name"));
                poi.setLatitude((double) poiEntry.get("latitude"));
                poi.setLongitude((double) poiEntry.get("longitude"));
                poi.setBrailleMenu((boolean) poiEntry.get("brailleMenu"));
                poi.setRollInShower((boolean) poiEntry.get("rollInShower"));
                poi.setAcceessibleParking((boolean) poiEntry.get("acceessibleParking"));
                poi.setInRoomAccessibility((boolean) poiEntry.get("inRoomAccessibility"));
                poi.setPrimaryFunctionsAvailable((boolean) poiEntry.get("primaryFunctionsAvailable"));
                poi.setSignLanguage((boolean) poiEntry.get("signLanguage"));
                poi.setStepFreeEntrance((boolean) poiEntry.get("stepFreeEntrance"));
                poi.setWheelchairRestrooms((boolean) poiEntry.get("wheelchairRestrooms"));
                poi.setWideDoorsAvailable((boolean) poiEntry.get("wideDoorsAvailable"));
                poi.setVerified((boolean) poiEntry.get("verified"));

                Intent intent = new Intent(getActivity(),PoiActivity.class);
                intent.putExtra("POI",poi);
                intent.putExtra("id",poiId);
                startActivity(intent);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mMap.clear();

    }
}
