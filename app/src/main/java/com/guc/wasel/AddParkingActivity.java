package com.guc.wasel;


/**
 * Created by Zaher Abdelrahman on 4/21/2017.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * This Class defines the AddParkingMapActivity, this activity is used to allow users to add Parkings to a specific point of interest, The user can define whether this Parking is accessible or not
 */
public class AddParkingActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    private Marker marker;
    private Button button;
    private Button abortButton;
    private String id;
    private double latitude;
    private double longitude;
    ArrayList<Parking> parkings;
    private Switch isAccessibleSwitch;
    private AlertDialog alertDialog;

    /**
     *This method is executed once the activity is created, to define the layout of the activity, declare the activity views
     * Assign Views Listeners, defining the map fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_parking);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        id = (String) getIntent().getStringExtra("id");

        declareActivityViews();

        setButtonListener();

    }



    /**
     * This method is executedwhen the map fragment is ready to set map listeners, declare the mMap variable, show the main marker on the map
     * @param googleMap The Google Maps Object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);



        loadParkings();



        // Add a marker in Sydney and move the camera
        LatLng cairo = new LatLng(30.044, 31.3157);

        marker = mMap.addMarker(new MarkerOptions().position(cairo));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                marker.setPosition(latLng);
            }
        });

    }

    /**
     * This function is called when a info window of a marker on the map is clicked, the Parking referred by this marker is removed from the database
     * @param marker the clicked marker object
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        parkings.remove(Integer.parseInt((String) marker.getTag()));
        marker.remove();
        FirebaseDatabase.getInstance().getReference().child("poi").child(id).child("parkings").setValue(parkings);



    }

    /**
     * This method connect to the realtime database of Firebase and load all Parkings of the point of interest.
     */
    public void loadParkings(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map poiEntry = (Map) dataSnapshot.getValue();
                parkings = (ArrayList) poiEntry.get("parkings");
                longitude = (double) poiEntry.get("longitude");
                latitude = (double) poiEntry.get("latitude");
                LatLng latLng = new LatLng(latitude,longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                marker.setPosition(latLng);
                Marker markerParking= mMap.addMarker(new MarkerOptions().position(latLng).
                        title((String) poiEntry.get("name")).
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                if(parkings != null)
                    for(int i=0;i<parkings.size();i++){
                        Map parking =(Map) parkings.get(i);
                        double longitude =(double) parking.get("longitude");
                        double latitude = (double) parking.get("latitude");
                        Marker m = mMap.addMarker(new MarkerOptions().position( new LatLng(latitude,longitude)).title("Tap Here to Remove").
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));
                        m.setTag(""+i);
                    }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Build and returns an alertdialog that asks the user if he is sure to add a new Parking or not.
     * @return The Alert dialogue Built
     */
    private  AlertDialog buildParkingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddParkingActivity.this);
        builder.setTitle("Parking Confirmation");
        builder.setMessage("Are you sure you want to add a parking at this location");
        builder.setPositiveButton("Add Parking", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LatLng latLng = marker.getPosition();
                Parking parking = new Parking();
                parking.setAccessible(true);
                parking.setLatitude(latLng.latitude);
                parking.setLongitude(latLng.longitude);
                parking.setAccessible(isAccessibleSwitch.isChecked());
                if(parkings == null){
                    parkings = new ArrayList<Parking>();
                }
                parkings.add(parking);
                FirebaseDatabase.getInstance().getReference().child("poi").child(id).child("parkings").setValue(parkings);
                finish();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        return builder.create();
    }

    /**
     * This method is responsible of declaring the views instant variables of this Activity to their corresponding view in the layout files, using findViewById() method.
     */
    private void declareActivityViews(){
        button = (Button) findViewById(R.id.parking_location_submit);
        abortButton = (Button) findViewById(R.id.parking_location_abort);
        isAccessibleSwitch = (Switch) findViewById(R.id.parking_switch) ;
        alertDialog = buildParkingDialog();
    }

    /**
     * This function set the listeners of the Save and Abort Buttons, Save Button add a new Parking, Abort Button close the Activity.
     */
    private void setButtonListener(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();

            }
        });
        abortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddParkingActivity.this.finish();
            }
        });
    }

}
