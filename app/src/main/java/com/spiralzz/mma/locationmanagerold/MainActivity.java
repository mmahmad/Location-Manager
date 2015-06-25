package com.spiralzz.mma.locationmanagerold;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {


    private static final String TAG = MainActivity.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    private android.location.Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters


    Location l;

    String coordinates;

    ArrayList<String> history;

    public void gotoHistory(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void saveLocation(View view) {

        final EditText input = new EditText(this);


        new AlertDialog.Builder(this)
                .setTitle("Save location")
                .setMessage("Enter name of the location below:")
                .setView(input)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        String locationName = input.getText().toString();

                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());

                        com.spiralzz.mma.locationmanagerold.Location newLocation = new com.spiralzz.mma.locationmanagerold.Location(locationName, coordinates);

                        //com.spiralzz.mma.locationmanagerold.Location newLocation = new com.spiralzz.mma.locationmanagerold.Location("Old firing range", "33.714659, 73.170882");
                        db.addLocation(newLocation);

                        String log = "Location: " + newLocation.getName() + ": " + newLocation.getCoordinates() + " saved into db.";

                        Context context = getApplicationContext();
                        //CharSequence text = "Hello toast!";
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, newLocation.getName() + " with coordinates: " + newLocation.getCoordinates() + " have been saved", duration);
                        toast.show();


                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void Copy(View view) {

        ClipboardManager clipboard = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clip = ClipData.newPlainText("text", coordinates);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(getApplicationContext(), "Coordinates Copied",
                Toast.LENGTH_SHORT).show();
    }

    public void getLocation(View view) {


            createLocationRequest();
            displayLocation();
            togglePeriodicLocationUpdates();



    }

    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            // Changing the button text
            Button getLocationButton = (Button) findViewById(R.id.button_getLoc);
            getLocationButton.setText("Tap to stop locating");

            mRequestingLocationUpdates = true;

            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            // Changing the button text
            Button getLocationButton = (Button) findViewById(R.id.button_getLoc);
            getLocationButton.setText("Tap to locate");

            mRequestingLocationUpdates = false;

            // Stopping the location updates
            stopLocationUpdates();

            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);



    }


    @Override
    public void onConnected(Bundle bundle) {
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
    }

//    class myLocationListener implements LocationListener {
//
//            @Override
//            public void onLocationChanged(Location location) {
//                if (location != null) {
//
//                    TextView latitude = (TextView) findViewById(R.id.textView_Latitude);
//                    TextView longitude = (TextView) findViewById(R.id.textView_Longitude);
//
//                    double pLongitude = location.getLongitude();
//                    double pLatitude = location.getLatitude();
//                    latitude.setText(Double.toString(pLatitude));
//                    longitude.setText(Double.toString(pLongitude));
//
//                    coordinates = "" + latitude.getText() + ", " + longitude.getText();
//
//                }
//            }

    // @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                switch (status) {
//                    case LocationProvider.OUT_OF_SERVICE:
//                        Toast.makeText(getApplicationContext(),
//                                "Status Changed: Out of Service",
//                                Toast.LENGTH_SHORT)	.show();
//                        break;
//
//                    case LocationProvider.TEMPORARILY_UNAVAILABLE:
//
//                        Toast.makeText(getApplicationContext(),
//                                "Status Changed: Temporarily Unavailable",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//
//                    case LocationProvider.AVAILABLE:
//
//                        Toast.makeText(getApplicationContext(),
//                                "Status Changed: Available",
//                                Toast.LENGTH_SHORT).show();
//                        break;
//                }
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//                Toast.makeText(getApplicationContext(), "Gps Enabled",
//                        Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//                 /* bring up the GPS settings */
//                Intent intent = new Intent(	android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
//
//
//            }
//        }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView latitude = (TextView) findViewById(R.id.textView_Latitude);
        TextView longitude = (TextView) findViewById(R.id.textView_Longitude);
        latitude.setText("0");
        longitude.setText("0");

        if (checkPlayServices()){
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API).build();

        }


    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    //@Override
//    protected void onPause() {
//        super.onPause();
//        if (mGoogleApiClient != null)
//            stopLocationUpdates();
//    }


    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double dlatitude = mLastLocation.getLatitude();
            double dlongitude = mLastLocation.getLongitude();

            TextView latitude = (TextView) findViewById(R.id.textView_Latitude);
            TextView longitude = (TextView) findViewById(R.id.textView_Longitude);

            latitude.setText(String.valueOf(dlatitude));
            longitude.setText(String.valueOf(dlongitude));

            coordinates = String.valueOf(dlatitude) + "," + String.valueOf(dlongitude);


        } else {

            TextView latitude = (TextView) findViewById(R.id.textView_Latitude);
            TextView longitude = (TextView) findViewById(R.id.textView_Longitude);

            latitude.setText("Calculating...");
            longitude.setText("Calculating...");
//            Log.i(TAG, "Couldn't get the location. Make sure location is enabled on the device");
//
//            Context context = getApplicationContext();
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context, "Enable location services for an accurate location", duration);
//            toast.show();
//
//            new AlertDialog.Builder(this)
//                    .setTitle("Enable GPS?")
//                    .setMessage("Turning on GPS will provide an accurate location. Would you like to turn it on?")
//
//                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            /* bring up the GPS settings */
//                            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(intent);
//                        }
//                    })
//                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int which) {
//                            // do nothing
//                        }
//                    })
//
//                    .show();


        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "Google Play Services not installed", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }


    @Override
    public void onLocationChanged(Location location) {

        // Assign the new location
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();

    }
}

