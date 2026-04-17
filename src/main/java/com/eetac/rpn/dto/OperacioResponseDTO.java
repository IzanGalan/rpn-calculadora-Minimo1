package com.eetac.rpn.dto;

public class OperacioResponseDTO {
    private String id;
    private String idAlumne;
    private String expressio;
    private Double resultat;
    private String estat;

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