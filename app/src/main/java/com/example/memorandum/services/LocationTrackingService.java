package com.example.memorandum.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.Service;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.app.NotificationCompat;

import com.example.memorandum.MainActivity;
import com.example.memorandum.MyDatabaseHelper;
import com.example.memorandum.model.GeoPosition;
import com.example.memorandum.model.Memorandum;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LocationTrackingService extends Service implements LocationListener{
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private Location currentLocation = null;
    private LocationManager locationManager = null;
    public static String TAG = "Location";

    List<Memorandum> lstMemorandum;
    MyDatabaseHelper myDB;

    GeofencingClient geofencingClient;
    List<Location> geofenceList;
    private final int GEOFENCE_RADIUS = 50000;

    private String CHANNEL_ID = "My Notification";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Location Tracking Service","started");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        lstMemorandum = new ArrayList<>();
        geofenceList = new ArrayList<>();
        myDB = new MyDatabaseHelper(getBaseContext());
        //setData();
        geofencingClient = LocationServices.getGeofencingClient(getBaseContext());
        locationProvidersInit();
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void setData(){
        lstMemorandum.clear();
        geofenceList.clear();
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(getBaseContext(), "There is no event in schedule", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                String id = cursor.getString(0);
                String stato = cursor.getString(1);
                String titolo = cursor.getString(2);
                String data = cursor.getString(3);
                String ora = cursor.getString(4);
                Date d = new Date();
                /*try {
                    d=new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(data+" "+ora);
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
                String luogo = cursor.getString(5);
                Double latitudine = Double.valueOf(cursor.getString(6));
                Double longitudine = Double.valueOf(cursor.getString(7));
                GeoPosition geoPosition = new GeoPosition(luogo,latitudine,longitudine);
                String descrizione = cursor.getString(8);
                Memorandum m = new Memorandum(id,stato,titolo,d,geoPosition,descrizione);
                if (m.isActive()) {
                    lstMemorandum.add(m);
                    Location tmpLocation = new Location("");//provider name is unnecessary
                    tmpLocation.setLatitude(m.getLuogo().getLatitude());//your coords of course
                    tmpLocation.setLongitude(m.getLuogo().getLongitude());
                    geofenceList.add(tmpLocation);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG,
                "onLocationChanged(): Provider: " + location.getProvider()
                        + " Lat: " + location.getLatitude() + " Lng: "
                        + location.getLongitude() + " Accuracy: "
                        + location.getAccuracy());

        if (currentLocation == null)
            currentLocation = location;
        else if (isBetterLocation(location, currentLocation)) {
            Log.d(TAG, "onLocationChanged(): Updating Location ... ");
            currentLocation = location;
            setData();
            checkGeofenceEntrance();
        }

    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d(TAG, "onProviderEnabled()");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d(TAG, "onProviderDisabled()");
    }

    private void locationProvidersInit(){
        try {

            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

                Log.d(TAG,"Location Permission Granted ! Starting Localization Info Init ....");

                // Acquire a reference to the system Location Manager
                locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                if(locationManager == null){
                    Toast.makeText(this, "Location Manager not Available !", Toast.LENGTH_LONG).show();
                    return;
                }

                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.d(TAG, "GPS_PROVIDER is enabled !");

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    Log.d(TAG, "Last Known Location: " + lastKnownLocation);

                    if (lastKnownLocation != null) {
                        currentLocation = lastKnownLocation;
                    }

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

                } else
                    Toast.makeText(this, "GPS is not enabled !", Toast.LENGTH_LONG)
                            .show();

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    Log.d(TAG, "NETWORK_PROVIDER is enabled !");

                    Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                    Log.d(TAG, "Last Known Location: " + lastKnownLocation);

                    if (lastKnownLocation != null
                            && isBetterLocation(lastKnownLocation, currentLocation)) {
                        currentLocation = lastKnownLocation;
                    }

                    // Register the listener with the Location Manager to receive
                    // location updates
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                } else
                    Toast.makeText(this, "LOCATION NETWORK PROVIDER is not enabled !",
                            Toast.LENGTH_LONG).show();
            }else {
                Log.e(TAG,"Location Permission Not Granted !");
            }

        }catch (Exception e){
            Log.e(TAG,"Exception: " + e.getLocalizedMessage());
        }
    }

    /**
     * Determines whether one Location reading is better than the current
     * Location fix
     *
     * @param location
     *            The new Location that you want to evaluate
     * @param currentBestLocation
     *            The current Location fix, to which you want to compare the new
     *            one
     */
    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use
        // the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be
            // worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
                .getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and
        // accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate
                && isFromSameProvider) {
            return true;
        }
        return false;
    }

    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void checkGeofenceEntrance() {
        for (int i=0 ; i< geofenceList.size() ;i++) {
            Location l = geofenceList.get(i);
            if (currentLocation.distanceTo(l) <= GEOFENCE_RADIUS){
                NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationCompat.Builder notification = new NotificationCompat.Builder(getBaseContext(),CHANNEL_ID)
                        .setContentIntent(PendingIntent.getActivity(getBaseContext(), 0, new Intent(getBaseContext(), MainActivity.class), 0))
                        .setContentTitle(lstMemorandum.get(i).getTitolo())
                        .setContentText(lstMemorandum.get(i).getLuogo().getName())
                        .setSmallIcon(android.R.drawable.stat_notify_more);

                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Notification", NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(notificationChannel);

                notificationManager.notify(0, notification.build());
                //notificationManager.notify(0, notification);
            }
        }
    }
}
