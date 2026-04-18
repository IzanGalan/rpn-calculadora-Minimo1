package models;

public class Institut {
    private String idInstitut;
    private String nom;

    public Institut() {}

    public Institut(String idInstitut, String nom) {
        this.idInstitut = idInstitut;
        this.nom = nom;
    }

    public String getIdInstitut() { return idInstitut; }
    public void setIdInstitut(String idInstitut) { this.idInstitut = idInstitut; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
}