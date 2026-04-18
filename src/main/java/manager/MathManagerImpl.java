package manager;

import models.Alumne;
import models.Institut;
import models.Operacio;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class MathManagerImpl implements MathManager {

    private static MathManager instance;
    final static Logger logger = Logger.getLogger(MathManagerImpl.class.getName());

    // Estructures de dades
    private Map<String, Institut> instituts;    // idInstitut -> Institut
    private Map<String, Alumne> alumnes;         // idAlumne -> Alumne
    private Queue<Operacio> cua;                 // FIFO per processar en ordre d'arribada
    private List<Operacio> totesOperacions;      // totes (pendents + processades)

    // Component RPN
    private ReversePolishNotationImpl rpn;

    // Singleton
    private MathManagerImpl() {
        this.instituts = new HashMap<>();
        this.alumnes = new HashMap<>();
        this.cua = new LinkedList<>();
        this.totesOperacions = new ArrayList<>();
        this.rpn = new ReversePolishNotationImpl();
    }

    public static MathManager getInstance() {
        if (instance == null) instance = new MathManagerImpl();
        return instance;
    }

    @Override
    public void afegirInstitut(Institut institut) {
        validarInstitut(institut);
        logger.info("afegirInstitut: " + institut.getNom());
        instituts.put(institut.getIdInstitut(), institut);
        logger.info("afegirInstitut FI - total: " + instituts.size());
    }

    @Override
    public void afegirAlumne(Alumne alumne) throws Exception {
        validarAlumne(alumne);
        logger.info("afegirAlumne: " + alumne.getNom()
                + " idInstitut: " + alumne.getIdInstitut());

        if (!instituts.containsKey(alumne.getIdInstitut())) {
            logger.error("afegirAlumne ERROR: institut no existeix "
                    + alumne.getIdInstitut());
            throw new Exception("Institut no existeix: " + alumne.getIdInstitut());
        }

        alumnes.put(alumne.getIdAlumne(), alumne);
        logger.info("afegirAlumne FI - total: " + alumnes.size());
    }

    @Override
    public void requerirOperacio(Operacio operacio) throws Exception {
        validarOperacio(operacio);
        logger.info("requerirOperacio: idAlumne=" + operacio.getIdAlumne()
                + " expressio=" + operacio.getExpressio());

        if (!alumnes.containsKey(operacio.getIdAlumne())) {
            logger.error("requerirOperacio ERROR: alumne no existeix "
                    + operacio.getIdAlumne());
            throw new Exception("Alumne no existeix: " + operacio.getIdAlumne());
        }

        operacio.setEstat("PENDING");
        operacio.setResultat(null);
        cua.add(operacio);
        totesOperacions.add(operacio);

        logger.info("requerirOperacio FI - pendents: " + cua.size());
    }

    @Override
    public Operacio processarOperacio() throws Exception {
        logger.info("processarOperacio - pendents: " + cua.size());

        if (cua.isEmpty()) {
            logger.error("processarOperacio ERROR: cua buida");
            throw new Exception("No hi ha operacions pendents");
        }

        // Agafem la primera de la cua (FIFO)
        Operacio op = cua.poll();

        // Calculem amb RPN
        double resultat = rpn.process(op.getExpressio());
        op.setResultat(resultat);
        op.setEstat("PROCESSED");

        logger.info("processarOperacio FI - id=" + op.getIdOperacio()
                + " resultat=" + resultat);
        return op;
    }

    @Override
    public List<Operacio> llistatOperacionsPerInstitut(String idInstitut)
            throws Exception {
        validarText(idInstitut, "L'identificador de l'institut no pot ser buit");
        logger.info("llistatOperacionsPerInstitut: " + idInstitut);

        if (!instituts.containsKey(idInstitut)) {
            logger.error("ERROR: institut no existeix " + idInstitut);
            throw new Exception("Institut no existeix: " + idInstitut);
        }

        List<Operacio> res = totesOperacions.stream()
                .filter(op -> {
                    Alumne a = alumnes.get(op.getIdAlumne());
                    return a != null && a.getIdInstitut().equals(idInstitut);
                })
                .collect(Collectors.toList());

        logger.info("llistatOperacionsPerInstitut FI - total: " + res.size());
        return res;
    }

    @Override
    public List<Operacio> llistatOperacionsPerAlumne(String idAlumne)
            throws Exception {
        validarText(idAlumne, "L'identificador de l'alumne no pot ser buit");
        logger.info("llistatOperacionsPerAlumne: " + idAlumne);

        if (!alumnes.containsKey(idAlumne)) {
            logger.error("ERROR: alumne no existeix " + idAlumne);
            throw new Exception("Alumne no existeix: " + idAlumne);
        }

        List<Operacio> res = totesOperacions.stream()
                .filter(op -> op.getIdAlumne().equals(idAlumne))
                .collect(Collectors.toList());

        logger.info("llistatOperacionsPerAlumne FI - total: " + res.size());
        return res;
    }

    @Override
    public List<Institut> llistatInstitutsPorOperacions() {
        logger.info("llistatInstitutsPorOperacions INICI");

        List<Institut> res = instituts.values().stream()
                .sorted((i1, i2) -> {
                    long c1 = comptarOps(i1.getIdInstitut());
                    long c2 = comptarOps(i2.getIdInstitut());
                    return Long.compare(c2, c1); // descendent
                })
                .collect(Collectors.toList());

        logger.info("llistatInstitutsPorOperacions FI - total: " + res.size());
        return res;
    }

    private long comptarOps(String idInstitut) {
        return totesOperacions.stream()
                .filter(op -> {
                    Alumne a = alumnes.get(op.getIdAlumne());
                    return a != null && a.getIdInstitut().equals(idInstitut);
                })
                .count();
    }

    @Override
    public void clear() {
        instituts.clear();
        alumnes.clear();
        cua.clear();
        totesOperacions.clear();
    }

    @Override
    public int numOperacionsPendents() {
        return cua.size();
    }

    private void validarInstitut(Institut institut) {
        if (institut == null) {
            throw new IllegalArgumentException("L'institut no pot ser null");
        }
        validarText(institut.getIdInstitut(), "L'identificador de l'institut no pot ser buit");
        validarText(institut.getNom(), "El nom de l'institut no pot ser buit");
    }

    private void validarAlumne(Alumne alumne) {
        if (alumne == null) {
            throw new IllegalArgumentException("L'alumne no pot ser null");
        }
        validarText(alumne.getIdAlumne(), "L'identificador de l'alumne no pot ser buit");
        validarText(alumne.getNom(), "El nom de l'alumne no pot ser buit");
        validarText(alumne.getCognoms(), "Els cognoms de l'alumne no poden ser buits");
        validarText(alumne.getIdInstitut(), "L'identificador de l'institut no pot ser buit");
    }

    private void validarOperacio(Operacio operacio) {
        if (operacio == null) {
            throw new IllegalArgumentException("L'operacio no pot ser null");
        }
        validarText(operacio.getIdOperacio(), "L'identificador de l'operacio no pot ser buit");
        validarText(operacio.getIdAlumne(), "L'identificador de l'alumne no pot ser buit");
        validarText(operacio.getExpressio(), "L'expressio no pot ser buida");
    }

    private void validarText(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(message);
        }
    }
}
