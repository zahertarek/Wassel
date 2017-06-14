package com.guc.wasel;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;


/**
 * This class defines the FormFragment, this fragment allows any user to add a new POI to the database.
 */
public class Form extends Fragment {

    ArrayList<Integer> places;
    Poi poi;
    private RadioGroup radioGroup;
    private AccessibilityLevel accessibilityLevel;
    CheckBox accessibleParkingBox;
    CheckBox stepFreeEntranceBox;
    CheckBox wideDoorsBox ;
    CheckBox primaryFunctionsBox;
    CheckBox wheelChairRestroomsBox;
    CheckBox inRoomAccessbilityBox ;
    CheckBox rollInShowerBox ;
    CheckBox brailleMenuBox;
    CheckBox signLanguageBox;
    EditText accessibleParkingEdit;
    EditText stepFreeEntranceEdit;
    EditText wideDoorsEdit;
    EditText primaryFunctionsEdit;
    EditText wheelChairRestroomsEdit;
    EditText inRoomAccessbilityEdit;
    EditText rollInShowerEdit;
    EditText brailleMenuEdit;
    EditText signLanguageEdit;
    AutoCompleteTextView edittext;
    LinearLayout linearLayout;
    Button locationButton;
    ArrayAdapter<String> arrayAdapter;


    /**
     * This is the default constructor of the fragment
     */
    public Form() {
        places = new ArrayList<Integer>();
        poi = new Poi();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview=  inflater.inflate(R.layout.fragment_form, container, false);

        declareActivityViews(rootview);

        String[] types = getResources().getStringArray(R.array.types);
        arrayAdapter =  new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,types);
        edittext.setAdapter(arrayAdapter);

        setRadioGroupListeners();



        setEditTextListener();


        addCheckBoxListeners();


        setLocationButtonListeners();


        return rootview;
    }

    /**
     * This method is responsible for assigning the listeners of the checkboxes shown i the fragment.
     */
    private  void addCheckBoxListeners(){
        accessibleParkingBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    accessibleParkingEdit.setVisibility(View.VISIBLE);
                }else{
                    accessibleParkingEdit.setVisibility(View.GONE);
                }
            }
        });


        stepFreeEntranceBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    stepFreeEntranceEdit.setVisibility(View.VISIBLE);
                else
                    stepFreeEntranceEdit.setVisibility(View.GONE);
            }
        });

        wideDoorsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    wideDoorsEdit.setVisibility(View.VISIBLE);
                else
                    wideDoorsEdit.setVisibility(View.GONE);
            }
        });

        primaryFunctionsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    primaryFunctionsEdit.setVisibility(View.VISIBLE);
                else
                    primaryFunctionsEdit.setVisibility(View.GONE);
            }
        });

        wheelChairRestroomsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    wheelChairRestroomsEdit.setVisibility(View.VISIBLE);
                else
                    wheelChairRestroomsEdit.setVisibility(View.GONE);
            }
        });

        inRoomAccessbilityBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    inRoomAccessbilityEdit.setVisibility(View.VISIBLE);
                else
                    inRoomAccessbilityEdit.setVisibility(View.GONE);
            }
        });

        rollInShowerBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    rollInShowerEdit.setVisibility(View.VISIBLE);
                else
                    rollInShowerEdit.setVisibility(View.GONE);
            }
        });

        brailleMenuBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                    brailleMenuEdit.setVisibility(View.VISIBLE);
                else
                    brailleMenuEdit.setVisibility(View.GONE);
            }
        });

        signLanguageBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    signLanguageEdit.setVisibility(View.VISIBLE);
                else
                    signLanguageEdit.setVisibility(View.GONE);
            }
        });

















    }

    /**
     * This method declares the fragment views instant variable to their corresponding views in the xml file.
     * @param rootview the rootview of the fragment.
     */
    private void declareActivityViews(View rootview){
        locationButton = (Button) rootview.findViewById(R.id.location_button);

        radioGroup = (RadioGroup) rootview.findViewById(R.id.radio_group);

        accessibleParkingBox = (CheckBox) rootview.findViewById(R.id.accessible_parking_checkbox);
        stepFreeEntranceBox= (CheckBox) rootview.findViewById(R.id.step_free_entrance_checkbox);
        wideDoorsBox = (CheckBox) rootview.findViewById(R.id.wide_doors_checkbox);
        primaryFunctionsBox = (CheckBox) rootview.findViewById(R.id.primary_functions_checkbox);
        wheelChairRestroomsBox = (CheckBox) rootview.findViewById(R.id.wheel_chair_restrooms_box);
        inRoomAccessbilityBox = (CheckBox) rootview.findViewById(R.id.inRoom_accessbility_checkbox);
        rollInShowerBox = (CheckBox) rootview.findViewById(R.id.roll_in_shower_checkbox);
        brailleMenuBox = (CheckBox) rootview.findViewById(R.id.braille_menu_checkbox);
        signLanguageBox = (CheckBox) rootview.findViewById(R.id.sign_language_checkbox);

        accessibleParkingEdit = (EditText) rootview.findViewById(R.id.accessible_parking_edittext);
        stepFreeEntranceEdit= (EditText) rootview.findViewById(R.id.step_free_entrance_edittext);
        wideDoorsEdit = (EditText) rootview.findViewById(R.id.wide_doors_edittext);
        primaryFunctionsEdit = (EditText) rootview.findViewById(R.id.primary_functions_edittext);
        wheelChairRestroomsEdit = (EditText) rootview.findViewById(R.id.wheel_chair_restrooms_edittext);
        inRoomAccessbilityEdit = (EditText) rootview.findViewById(R.id.in_room_edittext);
        rollInShowerEdit = (EditText) rootview.findViewById(R.id.roll_in_shower_edittext);
        brailleMenuEdit = (EditText) rootview.findViewById(R.id.braille_menu_edittext);
        signLanguageEdit = (EditText) rootview.findViewById(R.id.sign_language_edittext);

        edittext = (AutoCompleteTextView) rootview.findViewById(R.id.autocomplete_types);

        linearLayout = (LinearLayout) rootview.findViewById(R.id.linear_layout);
    }

    /**
     * This method set the Listener of the radio group
     */
    private void setRadioGroupListeners(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.radio_low)
                    accessibilityLevel = AccessibilityLevel.LOW;
                else if(i == R.id.radio_medium)
                    accessibilityLevel = AccessibilityLevel.MEDIUM;
                else
                    accessibilityLevel = AccessibilityLevel.HIGH;
            }

        });
    }

    /**
     * This method sets the listener of the auto complete text box.
     */
    private void setEditTextListener(){
        edittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String category = ((TextView) view).getText().toString();
                arrayAdapter.remove(category);
                TextView textView = new TextView(getActivity());
                textView.setText(category);
                linearLayout.addView(textView);
                switch (category){
                    case "accounting" : places.add(Place.TYPE_ACCOUNTING); break;
                    case "airport" : places.add(Place.TYPE_AIRPORT);break;
                    case "amusement_park": places.add(Place.TYPE_AMUSEMENT_PARK);break;
                    case "aquarium": places.add(Place.TYPE_AQUARIUM);break;
                    case "art_gallery": places.add(Place.TYPE_ART_GALLERY);break;
                    case "atm": places.add(Place.TYPE_ATM);break;
                    case "bakery": places.add(Place.TYPE_BAKERY);break;
                    case "bank": places.add(Place.TYPE_BANK);break;
                    case "bar": places.add(Place.TYPE_BAR);break;
                    case "beauty_salon": places.add(Place.TYPE_BEAUTY_SALON);break;
                    case "bicycle_store": places.add(Place.TYPE_BICYCLE_STORE);break;
                    case "book_store": places.add(Place.TYPE_BOOK_STORE);break;
                    case "bowling_alley": places.add(Place.TYPE_BOWLING_ALLEY);break;
                    case "bus_station": places.add(Place.TYPE_BUS_STATION);break;
                    case "cafe": places.add(Place.TYPE_CAFE);break;
                    case "campground": places.add(Place.TYPE_CAMPGROUND);break;
                    case "car_dealer": places.add(Place.TYPE_CAR_DEALER);break;
                    case "car_rental": places.add(Place.TYPE_CAR_RENTAL);break;
                    case "car_repair": places.add(Place.TYPE_CAR_REPAIR);break;
                    case "car_wash": places.add(Place.TYPE_CAR_WASH);break;
                    case "casino": places.add(Place.TYPE_CASINO);break;
                    case "cemetery": places.add(Place.TYPE_CEMETERY);break;
                    case "church": places.add(Place.TYPE_CHURCH);break;
                    case "city_hall": places.add(Place.TYPE_CITY_HALL);break;
                    case "clothing_store": places.add(Place.TYPE_CLOTHING_STORE);break;
                    case "convenience_store": places.add(Place.TYPE_CONVENIENCE_STORE);break;
                    case "courthouse": places.add(Place.TYPE_COURTHOUSE);break;
                    case "dentist": places.add(Place.TYPE_DENTIST);break;
                    case "department_store": places.add(Place.TYPE_DEPARTMENT_STORE);break;
                    case "doctor": places.add(Place.TYPE_DOCTOR);break;
                    case "electrician": places.add(Place.TYPE_ELECTRICIAN);break;
                    case "electronics_store": places.add(Place.TYPE_ELECTRONICS_STORE);break;
                    case "embassy": places.add(Place.TYPE_EMBASSY);break;
                    case "fire_station": places.add(Place.TYPE_FIRE_STATION);break;
                    case "florist": places.add(Place.TYPE_FLORIST);break;
                    case "funeral_home": places.add(Place.TYPE_FUNERAL_HOME);break;
                    case "furniture_store": places.add(Place.TYPE_FURNITURE_STORE);break;
                    case "gas_station": places.add(Place.TYPE_GAS_STATION);break;
                    case "gym": places.add(Place.TYPE_GYM);break;
                    case "hair_care": places.add(Place.TYPE_HAIR_CARE);break;
                    case "hardware_store": places.add(Place.TYPE_HARDWARE_STORE);break;
                    case "hindu_temple": places.add(Place.TYPE_HINDU_TEMPLE);break;
                    case "home_goods_store": places.add(Place.TYPE_HOME_GOODS_STORE);break;
                    case "hospital": places.add(Place.TYPE_HOSPITAL);break;
                    case "insurance_agency": places.add(Place.TYPE_INSURANCE_AGENCY);break;
                    case "jewelry_store": places.add(Place.TYPE_JEWELRY_STORE);break;
                    case "laundry": places.add(Place.TYPE_LAUNDRY);break;
                    case "lawyer": places.add(Place.TYPE_LAWYER);break;
                    case "library": places.add(Place.TYPE_LIBRARY);break;
                    case "liquor_store": places.add(Place.TYPE_LIQUOR_STORE);break;
                    case "local_government_office": places.add(Place.TYPE_LOCAL_GOVERNMENT_OFFICE);break;
                    case "locksmith": places.add(Place.TYPE_LOCKSMITH);break;
                    case "lodging": places.add(Place.TYPE_LODGING);break;
                    case "meal_delivery": places.add(Place.TYPE_MEAL_DELIVERY);break;
                    case "meal_takeaway": places.add(Place.TYPE_MEAL_TAKEAWAY);break;
                    case "mosque": places.add(Place.TYPE_MOSQUE);break;
                    case "movie_rental": places.add(Place.TYPE_MOVIE_RENTAL);break;
                    case "movie_theater": places.add(Place.TYPE_MOVIE_THEATER);break;
                    case "moving_company": places.add(Place.TYPE_MOVING_COMPANY);break;
                    case "museum": places.add(Place.TYPE_MUSEUM);break;
                    case "night_club": places.add(Place.TYPE_NIGHT_CLUB);break;
                    case "painter": places.add(Place.TYPE_PAINTER);break;
                    case "park": places.add(Place.TYPE_PARK);break;
                    case "parking": places.add(Place.TYPE_PARKING);break;
                    case "pet_store": places.add(Place.TYPE_PET_STORE);break;
                    case "pharmacy": places.add(Place.TYPE_PHARMACY);break;
                    case "physiotherapist": places.add(Place.TYPE_PHYSIOTHERAPIST);break;
                    case "plumber": places.add(Place.TYPE_PLUMBER);break;
                    case "police": places.add(Place.TYPE_POLICE);break;
                    case "post_office": places.add(Place.TYPE_POST_OFFICE);break;
                    case "real_estate_agency": places.add(Place.TYPE_REAL_ESTATE_AGENCY);break;
                    case "restaurant": places.add(Place.TYPE_RESTAURANT);break;
                    case "roofing_contractor": places.add(Place.TYPE_ROOFING_CONTRACTOR);break;
                    case "rv_park": places.add(Place.TYPE_RV_PARK);break;
                    case "school": places.add(Place.TYPE_SCHOOL);break;
                    case "shoe_store": places.add(Place.TYPE_SHOE_STORE);break;
                    case "shopping_mall": places.add(Place.TYPE_SHOPPING_MALL);break;
                    case "spa": places.add(Place.TYPE_SPA);break;
                    case "stadium": places.add(Place.TYPE_STADIUM);break;
                    case "storage": places.add(Place.TYPE_STORAGE);break;
                    case "store": places.add(Place.TYPE_STORE);break;
                    case "subway_station": places.add(Place.TYPE_SUBWAY_STATION);break;
                    case "synagogue": places.add(Place.TYPE_SYNAGOGUE);break;
                    case "taxi_stand": places.add(Place.TYPE_TAXI_STAND);break;
                    case "train_station": places.add(Place.TYPE_TRAIN_STATION);break;
                    case "transit_station": places.add(Place.TYPE_TRANSIT_STATION);break;
                    case "travel_agency": places.add(Place.TYPE_TRAVEL_AGENCY);break;
                    case "university": places.add(Place.TYPE_UNIVERSITY);break;
                    case "veterinary_care": places.add(Place.TYPE_VETERINARY_CARE);break;
                    case "zoo": places.add(Place.TYPE_ZOO);break;
                }

                edittext.setText("");
            }
        });
    }

    /**
     * This method assigns a listener to the setLocation Button.
     */
    private void setLocationButtonListeners(){
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = ((EditText) getActivity().findViewById(R.id.poi_name)).getText().toString();
                ArrayList<Gate> gates = new ArrayList<Gate>();
                ArrayList<Parking> parkings = new ArrayList<Parking>();
                ArrayList<Restroom> restrooms = new ArrayList<Restroom>();
                ArrayList<Integer> categories = (ArrayList<Integer>) places.clone();
                poi.setName(name);
                poi.setCategory(categories);
                poi.setGates(gates);
                poi.setRestrooms(restrooms);
                poi.setParkings(parkings);
                poi.setAccessbilityLevel(accessibilityLevel);
                if(accessibleParkingBox.isChecked())
                    poi.setAcceessibleParking(true);
                if(stepFreeEntranceBox.isChecked())
                    poi.setStepFreeEntrance(true);
                if(wideDoorsBox.isChecked())
                    poi.setWideDoorsAvailable(true);
                if(primaryFunctionsBox.isChecked())
                    poi.setPrimaryFunctionsAvailable(true);
                if(wheelChairRestroomsBox.isChecked())
                    poi.setWheelchairRestrooms(true);
                if(inRoomAccessbilityBox.isChecked())
                    poi.setInRoomAccessibility(true);
                if(rollInShowerBox.isChecked())
                    poi.setRollInShower(true);
                if(brailleMenuBox.isChecked())
                    poi.setBrailleMenu(true);
                if(signLanguageBox.isChecked())
                    poi.setSignLanguage(true);

                poi.setAccessibileParkingText(accessibleParkingEdit.getText().toString());
                poi.setStepFreeEntranceText(stepFreeEntranceEdit.getText().toString());
                poi.setWideDoorsAvailableText(wideDoorsEdit.getText().toString());
                poi.setPrimaryFunctionsAvailableText(primaryFunctionsEdit.getText().toString());
                poi.setWheelchairRestroomsText(wheelChairRestroomsEdit.getText().toString());
                poi.setInRoomAccessibilityText(inRoomAccessbilityEdit.getText().toString());
                poi.setRollInShowerText(rollInShowerEdit.getText().toString());
                poi.setBrailleMenuText(brailleMenuEdit.getText().toString());
                poi.setSignLanguageText(signLanguageEdit.getText().toString());
                poi.setVerified(MainActivity.isAdmin());

                if(((EditText)getActivity().findViewById(R.id.poi_name)).getText().toString().isEmpty()){
                    Toast.makeText(getActivity(),"Please enter a valid name",Toast.LENGTH_LONG).show();
                }else if(radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(getActivity(),"Please select accessibility Level",Toast.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getActivity(), SetLocation.class);
                    intent.putExtra("POI", poi);
                    startActivity(intent);
                }
            }
        });
    }

}
