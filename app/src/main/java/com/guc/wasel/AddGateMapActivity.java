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

public class AddGateMapActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;
    private Marker marker;
    private Button button;
    private String id;
    private double latitude;
    private double longitude;
    ArrayList<Gate> gates;
    private Switch isAccessibleSwitch;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gate_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        id = (String) getIntent().getStringExtra("id");
        button = (Button) findViewById(R.id.gate_location_submit);
        isAccessibleSwitch = (Switch) findViewById(R.id.gate_switch) ;
        alertDialog = buildGateDialog();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();

            }
        });

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);



        loadGates();



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

    @Override
    public void onInfoWindowClick(Marker marker) {
        gates.remove(Integer.parseInt((String) marker.getTag()));
        marker.remove();
        FirebaseDatabase.getInstance().getReference().child("poi").child(id).child("gates").setValue(gates);



    }

    public void loadGates(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map poiEntry = (Map) dataSnapshot.getValue();
                gates = (ArrayList) poiEntry.get("gates");
                longitude = (double) poiEntry.get("longitude");
                latitude = (double) poiEntry.get("latitude");
                LatLng latLng = new LatLng(latitude,longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
                marker.setPosition(latLng);
                Marker markerGate = mMap.addMarker(new MarkerOptions().position(latLng).
                        title((String) poiEntry.get("name")).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.icom)));
                if(gates != null)
                    for(int i=0;i<gates.size();i++){
                        Map gate =(Map) gates.get(i);
                        double longitude =(double) gate.get("longitude");
                        double latitude = (double) gate.get("latitude");
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

    private  AlertDialog buildGateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(AddGateMapActivity.this);
        builder.setTitle("Gate Confirmation");
        builder.setMessage("Are you sure you want to add a gate at this location");
        builder.setPositiveButton("Add Gate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                LatLng latLng = marker.getPosition();
                Gate gate = new Gate();
                gate.setAccessible(true);
                gate.setLatitude(latLng.latitude);
                gate.setLongitude(latLng.longitude);
                gate.setAccessible(isAccessibleSwitch.isChecked());
                if(gates == null){
                    gates = new ArrayList<Gate>();
                }
                gates.add(gate);
                FirebaseDatabase.getInstance().getReference().child("poi").child(id).child("gates").setValue(gates);
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
}
