package com.guc.wasel;

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
 * This Class defines the AddRestroomMapActivity, this activity is used to allow users to add Restrooms to a specific point of interest, The user can define whether this Restroom is accessible or not
 */
public class AddRestroomActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    private Marker marker;
    private Button button;
    private Button abortButton;
    private String id;
    private double latitude;
    private double longitude;
    ArrayList<Restroom> restrooms;
    private Switch isAccessibleSwitch;
    private AlertDialog alertDialog;


    /**
     *This method is executed once the activity is created, to define the layout of the activity, declare the activity views, assign Views Listeners, defining the map fragment
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_restroom);
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



        loadRestrooms();



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
     * This function is called when a info window of a marker on the map is clicked, the Restroom referred by this marker is removed from the database
     * @param marker the clicked marker object
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
        restrooms.remove(Integer.parseInt((String) marker.getTag()));
        marker.remove();
        FirebaseDatabase.getInstance().getReference().child("poi").child(id).child("restrooms").setValue(restrooms);



    }

    /**
     * This method connect to the realtime database of Firebase and load all Restrooms of the point of interest.
     */
    public void loadRestrooms(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map poiEntry = (Map) dataSnapshot.getValue();
                restrooms = (ArrayList) poiEntry.get("restrooms");
                longitude = (double) poiEntry.get("longitude");
                latitude = (double) poiEntry.get("latitude");
                LatLng latLng = new LatLng(latitude,longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                marker.setPosition(latLng);
                Marker markerRestroom= mMap.addMarker(new MarkerOptions().position(latLng).
                        title((String) poiEntry.get("name")).
                        icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                if(restrooms != null)
                    for(int i=0;i<restrooms.size();i++){
                        Map restroom =(Map) restrooms.get(i);
                        double longitude =(double) restroom.get("longitude");
                        double latitude = (double) restroom.get("latitude");
                        Marker m = mMap.addMarker(new MarkerOptions().position( new LatLng(latitude,longitude)).title("Tap Here to Remove").
                                icon(BitmapDescriptorFactory.fromResource(R.drawable.exit)));
                        m.setTag(""+i);
                    }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Build and returns an alertdialog that asks the user if he is sure to add a new Restroom or not.
     * @return The Alert dialogue Built
     */
    private  AlertDialog buildRestroomDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRestroomActivity.this);
        builder.setTitle("Restroom Confirmation");
        builder.setMessage("Are you sure you want to add a restroom at this location");
        builder.setPositiveButton("Add Restroom", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LatLng latLng = marker.getPosition();
                Restroom restroom = new Restroom();
                restroom.setAccessible(true);
                restroom.setLatitude(latLng.latitude);
                restroom.setLongitude(latLng.longitude);
                restroom.setAccessible(isAccessibleSwitch.isChecked());
                if(restrooms == null){
                    restrooms = new ArrayList<Restroom>();
                }
                restrooms.add(restroom);
                FirebaseDatabase.getInstance().getReference().child("poi").child(id).child("restrooms").setValue(restrooms);
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
        button = (Button) findViewById(R.id.restroom_location_submit);
        abortButton = (Button) findViewById(R.id.restroom_location_abort);
        isAccessibleSwitch = (Switch) findViewById(R.id.restroom_switch) ;
        alertDialog = buildRestroomDialog();
    }

    /**
     * This function set the listeners of the Save and Abort Buttons, Save Button add a new Restroom, Abort Button close the Activity.
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
                AddRestroomActivity.this.finish();
            }
        });
    }
}
