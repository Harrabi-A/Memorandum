package com.example.memorandum;

import  com.example.memorandum.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Geocoder;
import android.location.Address;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.memorandum.databinding.ActivityMainBinding;
import com.example.memorandum.model.GeoPosition;
import com.example.memorandum.model.Memorandum;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertActivity extends AppCompatActivity {
    Context context;

    private Button btnInsert;
    private EditText txtTitolo;
    private EditText txtOra;
    private EditText txtData;
    private EditText txtLuogo;
    private EditText txtDescrizione;

    private Address a;
    private Date d = new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_activity);

        context = this;

        txtTitolo = findViewById(R.id.txtTitolo);
        txtData = findViewById(R.id.date);
        txtOra = findViewById(R.id.time);
        txtLuogo = findViewById(R.id.txtPlace);
        txtDescrizione = findViewById(R.id.txtDescription);

        Geocoder geocoder = new Geocoder(this);
        List<Address> listAddress = new ArrayList<>();
        try {
            listAddress =geocoder.getFromLocationName(txtLuogo.getText().toString().trim(),1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listAddress.size() !=0) {
            a = listAddress.get(0);
        }else {
        }

        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Address a = getAddress(txtLuogo.getText().toString().trim());

                /*try {
                    d=new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(txtData.getText().toString().trim()+" "+txtOra.getText().toString().trim());
                } catch (ParseException e) {
                    e.printStackTrace();
                }*/
                //GeoPosition geoPosition = new GeoPosition(txtLuogo.getText().toString().trim(), a.getLatitude(),a.getLongitude());
                //Memorandum m = new Memorandum("Active",txtTitolo.getText().toString().trim(),
                        //d ,geoPosition, txtDescrizione.getText().toString().trim());
                GeoPosition geoPosition = new GeoPosition(a.getFeatureName(),a.getLatitude(),a.getLongitude());
                Date date = new Date();
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(txtData.getText().toString().trim()+" "+ txtOra.getText().toString().trim());
                    //date_debug = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("18/02/2020 12:30");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Memorandum m = new Memorandum("Active",txtTitolo.getText().toString().trim(),date,geoPosition,txtDescrizione.getText().toString().trim());
                MyDatabaseHelper myDB = new MyDatabaseHelper(InsertActivity.this);
                myDB.addMemorandum(m);

                Intent intent = new Intent(context, MainActivity.class);
                Intent i = new Intent();
                startActivity(intent);
            }
        });

    }

    public Address getAddress(String locName){
        Geocoder geocoder = new Geocoder(this.context);
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(locName,1);
            if (addressList != null){
                Log.d("debug", addressList.get(0).toString());
                return addressList.get(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}