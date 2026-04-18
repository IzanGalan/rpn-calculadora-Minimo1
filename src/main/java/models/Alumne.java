package models;

public class Alumne {
    private String idAlumne;
    private String nom;
    private String cognoms;
    private String idInstitut;

    public Alumne() {}

    public Alumne(String idAlumne, String nom, String cognoms, String idInstitut) {
        this.idAlumne = idAlumne;
        this.nom = nom;
        this.cognoms = cognoms;
        this.idInstitut = idInstitut;
    }

    public String getIdAlumne() { return idAlumne; }
    public void setIdAlumne(String idAlumne) { this.idAlumne = idAlumne; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getCognoms() { return cognoms; }
    public void setCognoms(String cognoms) { this.cognoms = cognoms; }

    public String getIdInstitut() { return idInstitut; }
    public void setIdInstitut(String idInstitut) { this.idInstitut = idInstitut; }
}