package com.guc.wasel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class EditPoiActivity extends AppCompatActivity {

    private Poi poi;
    private String id;
    private Switch parkingSwitch ;
    private Switch primarySwitch ;
    private Switch rollInSwitch ;
    private Switch signLanguageSwitch;
    private Switch stepFreeSwitch;
    private Switch wheelChaiSwitch ;
    private Switch wideDoorsSwitch;
    private Switch inRoomSwitch;
    private TextView poiName;
    ArrayList<Gate> gates;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poi);
        id = (String) getIntent().getStringExtra("id");

        ((Button) findViewById(R.id.add_gate_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),AddGateMapActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        poi = (Poi) getIntent().getSerializableExtra("POI");
        parkingSwitch = (Switch) findViewById(R.id.parking_toggle);
        primarySwitch= (Switch) findViewById(R.id.primary_toggle);
        rollInSwitch = (Switch) findViewById(R.id.shower_toggle);
        signLanguageSwitch= (Switch) findViewById(R.id.sign_toggle);
        stepFreeSwitch = (Switch) findViewById(R.id.step_free_toggle);
        wheelChaiSwitch = (Switch) findViewById(R.id.wheel_toggle);
        wideDoorsSwitch = (Switch) findViewById(R.id.wide_doors_toggle);
        inRoomSwitch = (Switch) findViewById((R.id.in_room_toggle));
        poiName = (TextView) findViewById(R.id.poi_name_text);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map poiEntry = (Map) dataSnapshot.getValue();

                parkingSwitch.setChecked((boolean) poiEntry.get("acceessibleParking"));
                primarySwitch.setChecked((boolean) poiEntry.get("primaryFunctionsAvailable"));
                rollInSwitch.setChecked((boolean) poiEntry.get("rollInShower"));
                signLanguageSwitch.setChecked((boolean) poiEntry.get("signLanguage"));
                stepFreeSwitch.setChecked((boolean) poiEntry.get("stepFreeEntrance"));
                wheelChaiSwitch.setChecked((boolean) poiEntry.get("wheelchairRestrooms"));
                wideDoorsSwitch.setChecked((boolean) poiEntry.get("wideDoorsAvailable"));
                inRoomSwitch.setChecked((boolean) poiEntry.get("inRoomAccessibility"));
                poiName.setText((String) poiEntry.get("name"));
                gates = (ArrayList) poiEntry.get("gates");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Button saveButton = (Button) findViewById(R.id.save_btn);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(id);
                poi.setAcceessibleParking(parkingSwitch.isChecked());
                poi.setPrimaryFunctionsAvailable(primarySwitch.isChecked());
                poi.setRollInShower(rollInSwitch.isChecked());
                poi.setSignLanguage(signLanguageSwitch.isChecked());
                poi.setStepFreeEntrance(stepFreeSwitch.isChecked());
                poi.setWheelchairRestrooms(wheelChaiSwitch.isChecked());
                poi.setWideDoorsAvailable(wideDoorsSwitch.isChecked());
                poi.setInRoomAccessibility(inRoomSwitch.isChecked());
                poi.setName(poiName.getText().toString());
                poi.setGates(gates);
                ref.setValue(poi);
                finish();
            }
        });

    }


}
