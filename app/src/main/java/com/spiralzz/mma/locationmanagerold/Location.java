package com.spiralzz.mma.locationmanagerold;

/**
 * Created by MMA on 06/24/2015.
 */

public class Location {

    //private variables
    int _id;
    String _name;
    String _location_coordinates;

    // Empty constructor
    public Location() {

    }

    // constructor
    public Location(int id, String name, String coordinates) {
        this._id = id;
        this._name = name;
        this._location_coordinates = coordinates;
    }

    // constructor
    public Location(String name, String coordinates) {
        this._name = name;
        this._location_coordinates = coordinates;
    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting name
    public String getName() {
        return this._name;
    }

    // setting name
    public void setName(String name) {
        this._name = name;
    }

    // getting phone number
    public String getCoordinates() {
        return this._location_coordinates;
    }

    // setting phone number
    public void setCoordinates(String coordinates) {
        this._location_coordinates = coordinates;
    }
}



