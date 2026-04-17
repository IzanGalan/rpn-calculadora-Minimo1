package com.eetac.rpn.model;

public class Institut {

    private String id;
    private String nom;

    public Institut() {}

    public Institut(String id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}