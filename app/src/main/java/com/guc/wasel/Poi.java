package com.guc.wasel;

import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Zaher Acdelrahman on 2/27/2017.
 */

/**
 * This class defines the point of interest model.
 */
public class Poi implements Serializable {


    private String Name;
    private double latitude;
    private double longitude;
    private boolean acceessibleParking;
    private String  accessibileParkingText;
    private boolean stepFreeEntrance;
    private String stepFreeEntranceText;
    private boolean wideDoorsAvailable;
    private String wideDoorsAvailableText;
    private boolean PrimaryFunctionsAvailable;
    private String PrimaryFunctionsAvailableText;
    private boolean wheelchairRestrooms;
    private String wheelchairRestroomsText;
    private boolean inRoomAccessibility;
    private String inRoomAccessibilityText;
    private boolean rollInShower;
    private String rollInShowerText;
    private boolean brailleMenu;
    private String brailleMenuText;
    private boolean signLanguage;
    private String signLanguageText;
    private ArrayList<Gate> gates;
    private ArrayList<Restroom> restrooms;
    private ArrayList<Parking> parkings;
    private AccessibilityLevel accessbilityLevel;
    private ArrayList<Integer> category;
    private boolean isVerified;

    /**
     * This method returns if this POI is verified or not
     * @return boolean
     */
    public boolean isVerified(){
        return isVerified;
    }
    public void setVerified(boolean b){
        isVerified = b;
    }
    public String getSignLanguageText() {
        return signLanguageText;
    }

    public void setSignLanguageText(String signLanguageText) {
        this.signLanguageText = signLanguageText;
    }

    public String getAccessibileParkingText() {
        return accessibileParkingText;
    }

    public void setAccessibileParkingText(String accessibileParkingText) {
        this.accessibileParkingText = accessibileParkingText;
    }

    public String getStepFreeEntranceText() {
        return stepFreeEntranceText;
    }

    public void setStepFreeEntranceText(String stepFreeEntranceText) {
        this.stepFreeEntranceText = stepFreeEntranceText;
    }

    public String getWideDoorsAvailableText() {
        return wideDoorsAvailableText;
    }

    public void setWideDoorsAvailableText(String wideDoorsAvailableText) {
        this.wideDoorsAvailableText = wideDoorsAvailableText;
    }

    public String getPrimaryFunctionsAvailableText() {
        return PrimaryFunctionsAvailableText;
    }

    public void setPrimaryFunctionsAvailableText(String primaryFunctionsAvailableText) {
        PrimaryFunctionsAvailableText = primaryFunctionsAvailableText;
    }

    public String getWheelchairRestroomsText() {
        return wheelchairRestroomsText;
    }

    public void setWheelchairRestroomsText(String wheelchairRestroomsText) {
        this.wheelchairRestroomsText = wheelchairRestroomsText;
    }

    public String getInRoomAccessibilityText() {
        return inRoomAccessibilityText;
    }

    public void setInRoomAccessibilityText(String inRoomAccessibilityText) {
        this.inRoomAccessibilityText = inRoomAccessibilityText;
    }

    public String getRollInShowerText() {
        return rollInShowerText;
    }

    public void setRollInShowerText(String rollInShowerText) {
        this.rollInShowerText = rollInShowerText;
    }

    public String getBrailleMenuText() {
        return brailleMenuText;
    }

    public void setBrailleMenuText(String brailleMenuText) {
        this.brailleMenuText = brailleMenuText;
    }



    public AccessibilityLevel getAccessbilityLevel() {
        return accessbilityLevel;
    }

    public void setAccessbilityLevel(AccessibilityLevel accessbilityLevel) {
        this.accessbilityLevel = accessbilityLevel;
    }



    public ArrayList<Gate> getGates() {
        return gates;
    }

    public void setGates(ArrayList<Gate> gates) {
        this.gates = gates;
    }


    public Poi(){

    }

    public ArrayList<Restroom> getRestrooms() {
        return restrooms;
    }

    public void setRestrooms(ArrayList<Restroom> restrooms) {
        this.restrooms = restrooms;
    }

    public boolean isSignLanguage() {
        return signLanguage;
    }

    public void setSignLanguage(boolean signLanguage) {
        this.signLanguage = signLanguage;
    }

    public boolean isBrailleMenu() {
        return brailleMenu;
    }

    public void setBrailleMenu(boolean brailleMenu) {
        this.brailleMenu = brailleMenu;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isAcceessibleParking() {
        return acceessibleParking;
    }

    public void setAcceessibleParking(boolean acceessibleParking) {
        this.acceessibleParking = acceessibleParking;
    }

    public boolean isStepFreeEntrance() {
        return stepFreeEntrance;
    }

    public void setStepFreeEntrance(boolean stepFreeEntrance) {
        this.stepFreeEntrance = stepFreeEntrance;
    }

    public boolean isWideDoorsAvailable() {
        return wideDoorsAvailable;
    }

    public void setWideDoorsAvailable(boolean wideDoorsAvailable) {
        this.wideDoorsAvailable = wideDoorsAvailable;
    }

    public boolean isPrimaryFunctionsAvailable() {
        return PrimaryFunctionsAvailable;
    }

    public void setPrimaryFunctionsAvailable(boolean primaryFunctionsAvailable) {
        PrimaryFunctionsAvailable = primaryFunctionsAvailable;
    }

    public boolean isWheelchairRestrooms() {
        return wheelchairRestrooms;
    }

    public void setWheelchairRestrooms(boolean wheelchairRestrooms) {
        this.wheelchairRestrooms = wheelchairRestrooms;
    }

    public boolean isInRoomAccessibility() {
        return inRoomAccessibility;
    }

    public void setInRoomAccessibility(boolean inRoomAccessibility) {
        this.inRoomAccessibility = inRoomAccessibility;
    }

    public boolean isRollInShower() {
        return rollInShower;
    }

    public void setRollInShower(boolean rollInShower) {
        this.rollInShower = rollInShower;
    }

    public ArrayList<Integer> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<Integer> category) {
        this.category = category;
    }

    public ArrayList<Parking> getParkings() {
        return parkings;
    }

    public void setParkings(ArrayList<Parking> parkings) {
        this.parkings = parkings;
    }

    /**
     * This method save a new POI to the database.
     */
    public void writePoi(){
        DatabaseReference mDatabase;

            mDatabase = FirebaseDatabase.getInstance().getReference().child("poi").push();
            mDatabase.setValue(this);



    }


}