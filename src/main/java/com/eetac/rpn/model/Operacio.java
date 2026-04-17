package com.eetac.rpn.model;

public class Operacio {

    private String id;
    private String idAlumne;
    private String expressio;   // ex: "5 1 2 + 4 * + 3 -"
    private Double resultat;    // null fins que es processa
    private String estat;       // "PENDING" o "PROCESSED"

    public Operacio() {}

    public Operacio(String id, String idAlumne, String expressio) {
        this.id = id;
        this.idAlumne = idAlumne;
        this.expressio = expressio;
        this.resultat = null;
        this.estat = "PENDING";
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdAlumne() { return idAlumne; }
    public void setIdAlumne(String idAlumne) { this.idAlumne = idAlumne; }

    public String getExpressio() { return expressio; }
    public void setExpressio(String expressio) { this.expressio = expressio; }

    public Double getResultat() { return resultat; }
    public void setResultat(Double resultat) { this.resultat = resultat; }

    public String getEstat() { return estat; }
    public void setEstat(String estat) { this.estat = estat; }
}