package com.example.memorandum.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.database.Cursor;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.memorandum.InsertActivity;
import com.example.memorandum.MyDatabaseHelper;
import com.example.memorandum.R;
import com.example.memorandum.RecyclerViewAdapter;
import com.example.memorandum.databinding.ActivityMainBinding;
import com.example.memorandum.model.GeoPosition;
import com.example.memorandum.model.Memorandum;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActiveMemorandumFragment extends Fragment {
    View v;
    private RecyclerView myrecyclerview;
    List<Memorandum>  lstMemorandum;

    MyDatabaseHelper myDB;

    private ActivityMainBinding binding;

    public ActiveMemorandumFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lstMemorandum = new ArrayList<>();
        GeoPosition location = new GeoPosition("Parma",0,0);
        Date date_debug = new Date();
        /*lstMemorandum.add(new Memorandum("Esame1",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame2",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame1",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame2",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame1",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame2",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame1",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame2",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame1",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame2",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame1",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame2",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame1",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame2",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame1",date_debug,location));
        lstMemorandum.add(new Memorandum("Esame2",date_debug,location));*/
        myDB = new MyDatabaseHelper(this.getContext());
        //setData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setData();

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_active_memorandum, container, false);
        myrecyclerview = (RecyclerView) v.findViewById(R.id.active_mem_table);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), lstMemorandum);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(adapter);
        myrecyclerview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        adapter.notifyDataSetChanged();
        myrecyclerview.invalidate();
        FloatingActionButton fab2 = (FloatingActionButton) v.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInsertActivity();
            }
        });

        return v;
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

    public void openInsertActivity(){
        Intent intent = new Intent(getContext(), InsertActivity.class);
        startActivity(intent);
    }
}