package com.eetac.rpn.dto;

public class AlumneDTO {
    private String id;
    private String nom;
    private String cognoms;
    private String idInstitut;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getCognoms() { return cognoms; }
    public void setCognoms(String cognoms) { this.cognoms = cognoms; }
    public String getIdInstitut() { return idInstitut; }
    public void setIdInstitut(String idInstitut) { this.idInstitut = idInstitut; }
}