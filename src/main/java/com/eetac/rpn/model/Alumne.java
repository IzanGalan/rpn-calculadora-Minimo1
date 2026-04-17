package com.eetac.rpn.model;

public class Alumne {

    private String id;
    private String nom;
    private String cognoms;
    private String idInstitut;

    public Alumne() {}

    public Alumne(String id, String nom, String cognoms, String idInstitut) {
        this.id = id;
        this.nom = nom;
        this.cognoms = cognoms;
        this.idInstitut = idInstitut;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCognoms() { return cognoms; }
    public void setCognoms(String cognoms) { this.cognoms = cognoms; }

    public String getIdInstitut() { return idInstitut; }
    public void setIdInstitut(String idInstitut) { this.idInstitut = idInstitut; }
}