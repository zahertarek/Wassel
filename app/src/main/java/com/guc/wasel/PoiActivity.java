package com.guc.wasel;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * This Class defines the POiActivity , that show the detailed info of a specific POI
 */
public class PoiActivity extends AppCompatActivity implements OnMapReadyCallback {

    private Poi poi;
    private TextView nameText;
    private TextView accessibleParking;
    private TextView primaryFunctions;
    private TextView rollInShower;
    private TextView signLanguage;
    private TextView stepFreeEntrance;
    private TextView wheelChairRestrooms;
    private TextView inRoomAccessbility;
    private TextView wideDoors;
    private ImageView accessibleParkinImg;
    private ImageView primaryFunctionsImg;
    private ImageView rollInShowerImg;
    private ImageView signLanguageImg;
    private ImageView stepFreeEntranceImg;
    private ImageView wheelChairRestroomsImg;
    private ImageView inRoomAccessbilityImg;
    private ImageView wideDoorsImg;
    private ExpandableTextView accessibleParkingExp;
    private ExpandableTextView primaryFunctionsExp;
    private ExpandableTextView rollInShowerExp;
    private ExpandableTextView signLanguageExp;
    private ExpandableTextView stepFreeEntranceExp;
    private ExpandableTextView wheelChairRestroomsExp;
    private ExpandableTextView inRoomAccessbilityExp;
    private ExpandableTextView wideDoorsExp;
    private GoogleMap mMap;
    private Button editButton;
    private Button deleteButton;
    private Button verifyButton;
    private AlertDialog verifyDialog;
    private AlertDialog deleteDialog;
    String poiId;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poi);
        declareActivityViews();
        checkUserStatus();
        initializeScrollView();




        poi = (Poi) getIntent().getSerializableExtra("POI");
        poiId = getIntent().getStringExtra("id");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSupportActionBar().setTitle(poi.getName());



        setViewsValue();


        checkIfAdmin();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadGates();
        loadRestrooms();
        loadParkings();
    }

    /**
     * This method is responsible on showing google maps zoomed on the current POI, showing the gates, restroom, and parkings
     * @param googleMap google maps object
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        loadGates();
        loadRestrooms();
        loadParkings();


    }

    @Override
    protected void onPause() {
        super.onPause();
        mMap.clear();
    }

    /**
     * This method connects to the database and load the gates of a specific POI
     */
    private void loadGates() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(poiId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map poiEntry = (Map) dataSnapshot.getValue();
                if(poiEntry!=null){
                    ArrayList gates = (ArrayList) poiEntry.get("gates");
                    double longitude = (double) poiEntry.get("longitude");
                    double latitude = (double) poiEntry.get("latitude");
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).
                            title((String) poiEntry.get("name")).
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    if (gates != null)
                        for (int i = 0; i < gates.size(); i++) {
                            Map gate = (Map) gates.get(i);
                            double longitude1 = (double) gate.get("longitude");
                            double latitude1 = (double) gate.get("latitude");
                            boolean isAccessible = (boolean) gate.get("accessible");
                            String accessible = (isAccessible)?"Accessible Gate":"Not Accessible Gate";


                            Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.exit)).title(accessible));
                        }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method connects to the database and load the parkings of a specific POI
     */
    private void loadParkings() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(poiId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map poiEntry = (Map) dataSnapshot.getValue();
                if(poiEntry!=null){
                    ArrayList parkings = (ArrayList) poiEntry.get("parkings");
                    double longitude = (double) poiEntry.get("longitude");
                    double latitude = (double) poiEntry.get("latitude");
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
                    Marker marker = mMap.addMarker(new MarkerOptions().position(latLng).
                            title((String) poiEntry.get("name")).
                            icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
                    if (parkings != null)
                        for (int i = 0; i < parkings.size(); i++) {
                            Map parking = (Map) parkings.get(i);
                            double longitude1 = (double) parking.get("longitude");
                            double latitude1 = (double) parking.get("latitude");
                            boolean isAccessible = (boolean) parking.get("accessible");
                            String accessible = (isAccessible)?"Accessible Parking":"Not Accessible Parking";


                            Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)).title(accessible));
                        }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method connects to the database and load the restrooms of a specific POI
     */
    private void loadRestrooms() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(poiId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map poiEntry = (Map) dataSnapshot.getValue();
                if(poiEntry!=null){
                    ArrayList restrooms = (ArrayList) poiEntry.get("restrooms");
                    if (restrooms != null)
                        for (int i = 0; i < restrooms.size(); i++) {
                            Map restroom = (Map) restrooms.get(i);
                            double longitude1 = (double) restroom.get("longitude");
                            double latitude1 = (double) restroom.get("latitude");
                            boolean isAccessible= (boolean) restroom.get("accessible");
                            String accessible =(isAccessible)?"Accessible Restroom":"Not Accessible Restroom";
                            Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude1, longitude1)).
                                    icon(BitmapDescriptorFactory.fromResource(R.drawable.restroom)).title(accessible));
                        }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }


    /**
     * This method builds an alert dialog to confirm on the user to verify a certain POI
     * @return alertDialog
     */
    private AlertDialog buildVerifyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PoiActivity.this);
        builder.setTitle("Confirm Verification");
        builder.setMessage("Are you sure you want to verify this place");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(poiId);
                poi.setVerified(true);
                ref.setValue(poi);
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
     * This method builds an alert dialog to confirm on the user to delete a certain POI
     * @return alertDialog
     */
    private AlertDialog buildDeleteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PoiActivity.this);
        builder.setTitle("Delete");
        builder.setMessage("This POI will be deleted permanently");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(poiId);
                ref.setValue(null);
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
     * This method check the current user status, and if he is an admin or normal user
     */
    private void checkUserStatus(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("User Status", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("User Status", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }


    /**
     * This method is responsible of declaring the views instant variables of this Activity to their corresponding view in the layout files, using findViewById() method.
     */
    private void declareActivityViews(){
        verifyDialog = buildVerifyDialog();
        deleteDialog = buildDeleteDialog();

        editButton = (Button) findViewById(R.id.edit_btn);
        deleteButton = (Button) findViewById(R.id.delete_btn);
        verifyButton = (Button) findViewById(R.id.verify_btn);

        nameText = (TextView) findViewById(R.id.poi_name_text);
        accessibleParking = (TextView) findViewById(R.id.parking_text);
        primaryFunctions = (TextView) findViewById(R.id.primary_functions_text);
        rollInShower = (TextView) findViewById(R.id.shower_text);
        signLanguage = (TextView) findViewById(R.id.sign_language_availability_text);
        stepFreeEntrance = (TextView) findViewById(R.id.step_free_text);
        wheelChairRestrooms = (TextView) findViewById(R.id.wheelchair_restrooms_text);
        wideDoors = (TextView) findViewById(R.id.wide_doors_text);
        inRoomAccessbility = (TextView) findViewById((R.id.in_room_text));

        accessibleParkingExp = (ExpandableTextView) findViewById(R.id.expand_text_view_parking);
        primaryFunctionsExp = (ExpandableTextView) findViewById(R.id.expand_text_view_primary_functions);
        rollInShowerExp = (ExpandableTextView) findViewById(R.id.expand_text_view_shower);
        signLanguageExp = (ExpandableTextView) findViewById(R.id.expand_text_view_sign);
        stepFreeEntranceExp = (ExpandableTextView) findViewById(R.id.expand_text_view_step_free);
        wheelChairRestroomsExp = (ExpandableTextView) findViewById(R.id.expand_text_view_wheel);
        wideDoorsExp = (ExpandableTextView) findViewById(R.id.expand_text_view_wide_doors);
        inRoomAccessbilityExp = (ExpandableTextView) findViewById(R.id.expand_text_view_inroom);

        accessibleParkinImg = (ImageView) findViewById(R.id.parking_img);
        primaryFunctionsImg = (ImageView) findViewById(R.id.primary_functions_img);
        rollInShowerImg = (ImageView) findViewById(R.id.shower_img);
        signLanguageImg = (ImageView) findViewById(R.id.sign_img);
        stepFreeEntranceImg = (ImageView) findViewById(R.id.step_free_img);
        wheelChairRestroomsImg = (ImageView) findViewById(R.id.wheelchair_restrooms_img);
        wideDoorsImg = (ImageView) findViewById(R.id.wide_doors_img);
        inRoomAccessbilityImg = (ImageView) findViewById(R.id.in_room_img);
    }

    private void setViewsValue(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("poi").child(poiId);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map poiEntry = (Map) dataSnapshot.getValue();
                if(poiEntry!=null) {
                    String name = (String) poiEntry.get("name");
                    if ((boolean) poiEntry.get("rollInShower"))
                        rollInShowerImg.setImageResource(R.drawable.tick);
                    else
                        rollInShowerImg.setImageResource(R.drawable.cross);
                    if ((boolean) poiEntry.get("acceessibleParking"))
                        accessibleParkinImg.setImageResource(R.drawable.tick);
                    else
                        accessibleParkinImg.setImageResource(R.drawable.cross);
                    if ((boolean) poiEntry.get("inRoomAccessibility"))
                        inRoomAccessbilityImg.setImageResource(R.drawable.tick);
                    else
                        inRoomAccessbilityImg.setImageResource(R.drawable.cross);
                    if ((boolean) poiEntry.get("primaryFunctionsAvailable"))
                        primaryFunctionsImg.setImageResource(R.drawable.tick);
                    else
                        primaryFunctionsImg.setImageResource(R.drawable.cross);
                    if ((boolean) poiEntry.get("signLanguage"))
                        signLanguageImg.setImageResource(R.drawable.tick);
                    else
                        signLanguageImg.setImageResource(R.drawable.cross);
                    if ((boolean) poiEntry.get("stepFreeEntrance"))
                        stepFreeEntranceImg.setImageResource(R.drawable.tick);
                    else
                        stepFreeEntranceImg.setImageResource(R.drawable.cross);
                    if ((boolean) poiEntry.get("wheelchairRestrooms"))
                        wheelChairRestroomsImg.setImageResource(R.drawable.tick);
                    else
                        wheelChairRestroomsImg.setImageResource(R.drawable.cross);
                    if ((boolean) poiEntry.get("wideDoorsAvailable"))
                        wideDoorsImg.setImageResource(R.drawable.tick);
                    else
                        wideDoorsImg.setImageResource(R.drawable.cross);

                    String rollInShowerDes = (String) poiEntry.get("rollInShowerText");
                    String parkingTextDes = (String) poiEntry.get("accessibileParkingText");
                    String inRoomTextDes = (String) poiEntry.get("inRoomAccessibilityText");
                    String primaryFunctionsTextDes = (String) poiEntry.get("primaryFunctionsAvailableText");
                    String signLanguageTextDes = (String) poiEntry.get("signLanguageText");
                    String stepFreeTextDes = (String) poiEntry.get("stepFreeEntranceText");
                    String wheelchairTextDes = (String) poiEntry.get("wheelchairRestroomsText");
                    String wideDoorsTextDes = (String) poiEntry.get("wideDoorsAvailableText");


                    rollInShowerExp.setText(rollInShowerDes);
                    accessibleParkingExp.setText(parkingTextDes);
                    inRoomAccessbilityExp.setText(inRoomTextDes);
                    primaryFunctionsExp.setText(primaryFunctionsTextDes);
                    signLanguageExp.setText(signLanguageTextDes);
                    stepFreeEntranceExp.setText(stepFreeTextDes);
                    wheelChairRestroomsExp.setText(wheelchairTextDes);
                    wideDoorsExp.setText(wideDoorsTextDes);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method adjust the scrolling behaviour of the current activity
     */
    private void initializeScrollView(){
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);
        ImageView imageView = (ImageView) findViewById(R.id.transparent_image);

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        // Disable touch on transparent view
                        return false;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        scrollView.requestDisallowInterceptTouchEvent(false);
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        scrollView.requestDisallowInterceptTouchEvent(true);
                        return false;

                    default:
                        return true;
                }
            }

        });
    }

    /**
     * This methofd adjust the visibility of certain button based on the user status
     */
    private void checkIfAdmin(){
        if (MainActivity.isAdmin()) {
            editButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            if (!poi.isVerified())
                //Toast.makeText(getBaseContext(),""+poi.isVerified(),Toast.LENGTH_LONG).show();
                verifyButton.setVisibility(View.VISIBLE);
        }


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), EditPoiActivity.class);
                intent.putExtra("POI", poi);
                intent.putExtra("id", poiId);
                startActivity(intent);
            }
        });

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyDialog.show();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteDialog.show();
            }
        });
    }
}