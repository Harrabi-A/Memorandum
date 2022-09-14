package com.example.memorandum.model;

import java.util.Date;

public class Memorandum {
    private String id;
    private String titolo;
    private Date data;
    private GeoPosition luogo;
    private String descrizione;
    private String stato;

    public Memorandum(String i,String s ,String t, Date d, GeoPosition p, String desc){
        id = i;
        titolo = t;
        data = d;
        luogo = p;
        stato = s;
        descrizione = desc;
    }

    public Memorandum(String s ,String t, Date d, GeoPosition p, String desc){
        titolo = t;
        data = d;
        luogo = p;
        stato = s;
        descrizione = desc;
    }

    public Memorandum(String s,String t, Date d, GeoPosition p){
        titolo = t;
        data = d;
        luogo = p;
        stato = s;
        descrizione = "";
    }

    public Memorandum(String t, Date d, GeoPosition p, String desc){
        titolo = t;
        data = d;
        luogo = p;
        stato = "Active";
        descrizione = desc;
    }



    public Memorandum(String t, Date d, GeoPosition p){
        titolo = t;
        data = d;
        luogo = p;
        stato = "Active";
        descrizione = "";
    }

    public boolean isActive(){
        if (stato.equals("Active")){
            return true;
        }else {
            return false;
        }
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public GeoPosition getLuogo() {
        return luogo;
    }

    public void setLuogo(GeoPosition luogo) {
        this.luogo = luogo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getStato() {
        return stato;
    }

    public void setStato(String stato) {
        this.stato = stato;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
