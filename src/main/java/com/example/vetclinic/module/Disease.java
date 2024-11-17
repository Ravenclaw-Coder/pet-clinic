package com.example.vetclinic.module;

public class Disease {
    private int id;
    private String commonName;
    private String scientificName;

    public Disease(int id, String commonName, String scientificName) {
        this.id = id;
        this.commonName = commonName;
        this.scientificName = scientificName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }
}
