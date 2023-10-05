package com.example.demoprojekt;

public class ModelTabelleDaten {

    String name;
    String adresse;
    String datum;
    String abgesagt;

    public ModelTabelleDaten(String name, String datum, String adresse, String abgesagt) {
        this.name = name;
        this.datum = datum;
        this.adresse = adresse;
        this.abgesagt = abgesagt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getAbgesagt() {
        return abgesagt;
    }

    public void setAbgesagt(String abgesagt) {
        this.abgesagt = abgesagt;
    }
}
