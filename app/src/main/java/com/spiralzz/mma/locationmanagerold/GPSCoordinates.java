package com.spiralzz.mma.locationmanagerold;

/**
 * Created by MMA on 12/26/2014.
 */
public class GPSCoordinates {
    double xCoord;
    double yCoord;

    GPSCoordinates () {} // default ctor

    GPSCoordinates(int x, int y){
        xCoord = x;
        yCoord = y;
    }
    void setxCoord(int x){
        this.xCoord = x;
    }

    void setyCoord(int y){
        this.yCoord = y;
    }

    GPSCoordinates getCoordinates(){
       return this;
    }
}
