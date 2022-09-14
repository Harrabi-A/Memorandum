package com.example.memorandum.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.memorandum.MyDatabaseHelper;
import com.example.memorandum.R;

import com.example.memorandum.model.GeoPosition;
import com.example.memorandum.model.Memorandum;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MapsFragment extends Fragment {
    MyDatabaseHelper myDB;
    List<Memorandum> lstMemorandum;

    LocationManager locationManager;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            for (int i=0; i<lstMemorandum.size(); i++){
                Memorandum m_tmp = lstMemorandum.get(i);
                GeoPosition geoPosition = m_tmp.getLuogo();
                LatLng location = new LatLng(geoPosition.getLatitude(),geoPosition.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(location).title(m_tmp.getTitolo()));
            }

            /*LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        myDB = new MyDatabaseHelper(this.getContext());
        lstMemorandum = new ArrayList<>();
        setData();

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    void setData(){
        Cursor cursor = myDB.readAllData();
        if (cursor.getCount() == 0){
            Toast.makeText(this.getContext(), "There is no event in schedule", Toast.LENGTH_SHORT).show();
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
                }
            }
        }
    }
}