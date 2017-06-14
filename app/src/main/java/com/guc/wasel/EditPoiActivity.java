

package com.guc.wasel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
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

/**
 * This class defines the EditPoiActivity which allows the user to edit the info of a specific Point of Interest.
 */
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
    private RadioGroup radioGroup;
    private AccessibilityLevel accessibilityLevel;
    private String accessibilityLevelText;
    private Button saveButton;
    ArrayList<Gate> gates;
    ArrayList<Restroom> restrooms;
    ArrayList<Parking> parkings;

    /**
     * This method is executed once the activity is created, to define the layout of the activity, delare activity views, assign listeners to views
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poi);
        id = (String) getIntent().getStringExtra("id");


        setButtonsListeners();
        declareActivityViews();
        setRadioGroupListener();
        loadPoiFeatures();
        setSaveButtonListener();

    }

    /**
     * This method is responsible for assigning Listeners to every button in the activity
     */
    private void setButtonsListeners(){
        ((Button) findViewById(R.id.add_gate_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),AddGateMapActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.add_restroom_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),AddRestroomActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

        ((Button) findViewById(R.id.add_parking_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),AddParkingActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });
    }

    /**
     * This method is responsible to declaring the views instant variables of this activity to their corresponding views in the .xml file using the findViewById() methos
     */
    private void declareActivityViews(){
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
        radioGroup = (RadioGroup) findViewById(R.id.radio_group_edit);
        saveButton = (Button) findViewById(R.id.save_btn);
    }

    /**
     * This method is responsible for assigning the listener of the radioGroup in this activity
     */
    private void setRadioGroupListener(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radio_low_edit)
                    accessibilityLevel = AccessibilityLevel.LOW;
                else if(i == R.id.radio_medium_edit)
                    accessibilityLevel = AccessibilityLevel.MEDIUM;
                else
                    accessibilityLevel = AccessibilityLevel.HIGH;

            }
        });
    }

    /**
     * This method is responsible for connecting to the Firebase realtime database, to load the data of this specific, database
     */
    private void loadPoiFeatures(){
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
                restrooms = (ArrayList) poiEntry.get("restrooms");
                parkings = (ArrayList) poiEntry.get("parkings");
                accessibilityLevelText = (String) poiEntry.get("accessbilityLevel");
                if(accessibilityLevelText.equals("HIGH"))
                    radioGroup.check(R.id.radio_high_edit);
                else if(accessibilityLevelText.equals("MEDIUM"))
                    radioGroup.check(R.id.radio_medium_edit);
                else
                    radioGroup.check(R.id.radio_low_edit);
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method is responsible for setting the Listener of the save button, the listener connects to the database and update the loaded Point of Interest.
     */
    private void setSaveButtonListener(){
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
                poi.setRestrooms(restrooms);
                poi.setParkings(parkings);
                poi.setAccessbilityLevel(accessibilityLevel);
                ref.setValue(poi);
                finish();
            }
        });
    }
}
