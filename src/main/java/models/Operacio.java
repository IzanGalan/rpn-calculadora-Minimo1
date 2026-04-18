package models;

public class Operacio {
    private String idOperacio;
    private String idAlumne;
    private String expressio;   // "5 1 2 + 4 * + 3 -"
    private Double resultat;    // null fins que es processa
    private String estat;       // "PENDING" o "PROCESSED"

    public Operacio() {}

    public Operacio(String idOperacio, String idAlumne, String expressio) {
        this.idOperacio = idOperacio;
        this.idAlumne = idAlumne;
        this.expressio = expressio;
        this.resultat = null;
        this.estat = "PENDING";
    }

    public String getIdOperacio() { return idOperacio; }
    public void setIdOperacio(String idOperacio) { this.idOperacio = idOperacio; }

    public String getIdAlumne() { return idAlumne; }
    public void setIdAlumne(String idAlumne) { this.idAlumne = idAlumne; }

    public String getExpressio() { return expressio; }
    public void setExpressio(String expressio) { this.expressio = expressio; }

    public Double getResultat() { return resultat; }
    public void setResultat(Double resultat) { this.resultat = resultat; }

    public String getEstat() { return estat; }
    public void setEstat(String estat) { this.estat = estat; }
}