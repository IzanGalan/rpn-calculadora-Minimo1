package com.eetac.rpn.service;

import com.eetac.rpn.model.Alumne;
import com.eetac.rpn.model.Institut;
import com.eetac.rpn.model.Operacio;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class MathManagerImpl implements MathManager {

    private static final Logger logger =
            LogManager.getLogger(MathManagerImpl.class);

    private static MathManagerImpl instance;

    private final Map<String, Institut> instituts;

    private final Map<String, Alumne> alumnes;

    private final Queue<Operacio> coadOperacionsPendents;

    private final List<Operacio> totesLesOperacions;

    private final ReversePolishNotation rpn;

    private MathManagerImpl() {
        instituts = new HashMap<>();
        alumnes = new HashMap<>();
        coadOperacionsPendents = new LinkedList<>();
        totesLesOperacions = new ArrayList<>();
        rpn = new ReversePolishNotationImpl();
    }

    public static synchronized MathManagerImpl getInstance() {
        if (instance == null) {
            instance = new MathManagerImpl();
            logger.info("MathManagerImpl - nova instància creada (Singleton)");
        }
        return instance;
    }

    public static synchronized void resetInstance() {
        instance = null;
    }



    @Override
    public void afegirInstitut(Institut institut) {
        logger.info("afegirInstitut - INICI - id={}, nom={}",
                institut.getId(), institut.getNom());

        instituts.put(institut.getId(), institut);

        logger.info("afegirInstitut - FI - total instituts={}",
                instituts.size());
    }



    @Override
    public void afegirAlumne(Alumne alumne) throws Exception {
        logger.info("afegirAlumne - INICI - id={}, nom={}, idInstitut={}",
                alumne.getId(), alumne.getNom(), alumne.getIdInstitut());

        if (!instituts.containsKey(alumne.getIdInstitut())) {
            logger.error("afegirAlumne - ERROR: institut no existeix id={}",
                    alumne.getIdInstitut());
            throw new Exception("Institut no existeix: " + alumne.getIdInstitut());
        }

        alumnes.put(alumne.getId(), alumne);

        logger.info("afegirAlumne - FI - total alumnes={}", alumnes.size());
    }



    @Override
    public void requerirOperacio(Operacio operacio) throws Exception {
        logger.info("requerirOperacio - INICI - id={}, idAlumne={}, " +
                        "expressio={}", operacio.getId(), operacio.getIdAlumne(),
                operacio.getExpressio());

        if (!alumnes.containsKey(operacio.getIdAlumne())) {
            logger.error("requerirOperacio - ERROR: alumne no existeix id={}",
                    operacio.getIdAlumne());
            throw new Exception("Alumne no existeix: " + operacio.getIdAlumne());
        }

        operacio.setEstat("PENDING");
        operacio.setResultat(null);

        coadOperacionsPendents.add(operacio);
        totesLesOperacions.add(operacio);

        logger.info("requerirOperacio - FI - operacions pendents={}",
                coadOperacionsPendents.size());
    }



    @Override
    public Operacio processarOperacio() throws Exception {
        logger.info("processarOperacio - INICI - pendents={}",
                coadOperacionsPendents.size());

        if (coadOperacionsPendents.isEmpty()) {
            logger.error("processarOperacio - ERROR: no hi ha operacions pendents");
            throw new Exception("No hi ha operacions pendents de processar");
        }

        Operacio operacio = coadOperacionsPendents.poll();

        double resultat = rpn.process(operacio.getExpressio());
        operacio.setResultat(resultat);
        operacio.setEstat("PROCESSED");

        logger.info("processarOperacio - FI - id={}, resultat={}",
                operacio.getId(), resultat);

        return operacio;
    }



    @Override
    public List<Operacio> llistatOperacionsPerInstitut(String idInstitut)
            throws Exception {
        logger.info("llistatOperacionsPerInstitut - INICI - idInstitut={}",
                idInstitut);

        if (!instituts.containsKey(idInstitut)) {
            logger.error("llistatOperacionsPerInstitut - ERROR: " +
                    "institut no existeix id={}", idInstitut);
            throw new Exception("Institut no existeix: " + idInstitut);
        }

        List<Operacio> resultat = totesLesOperacions.stream()
                .filter(op -> {
                    Alumne alumne = alumnes.get(op.getIdAlumne());
                    return alumne != null &&
                            alumne.getIdInstitut().equals(idInstitut);
                })
                .collect(Collectors.toList());

        logger.info("llistatOperacionsPerInstitut - FI - total={}",
                resultat.size());
        return resultat;
    }


    @Override
    public List<Operacio> llistatOperacionsPerAlumne(String idAlumne)
            throws Exception {
        logger.info("llistatOperacionsPerAlumne - INICI - idAlumne={}",
                idAlumne);

        if (!alumnes.containsKey(idAlumne)) {
            logger.error("llistatOperacionsPerAlumne - ERROR: " +
                    "alumne no existeix id={}", idAlumne);
            throw new Exception("Alumne no existeix: " + idAlumne);
        }

        List<Operacio> resultat = totesLesOperacions.stream()
                .filter(op -> op.getIdAlumne().equals(idAlumne))
                .collect(Collectors.toList());

        logger.info("llistatOperacionsPerAlumne - FI - total={}",
                resultat.size());
        return resultat;
    }

    @Override
    public List<Institut> llistatInstitutsPorOperacions() {
        logger.info("llistatInstitutsPorOperacions - INICI");

        // Per cada institut, comptem quantes operacions han fet els seus alumnes
        List<Institut> resultat = instituts.values().stream()
                .sorted((i1, i2) -> {
                    long count1 = comptarOperacionsInstitut(i1.getId());
                    long count2 = comptarOperacionsInstitut(i2.getId());
                    return Long.compare(count2, count1); // descendent
                })
                .collect(Collectors.toList());

        logger.info("llistatInstitutsPorOperacions - FI - total instituts={}",
                resultat.size());
        return resultat;
    }

    // ── Mètode privat d'ajuda ────────────────────────────────────────
    private long comptarOperacionsInstitut(String idInstitut) {
        return totesLesOperacions.stream()
                .filter(op -> {
                    Alumne alumne = alumnes.get(op.getIdAlumne());
                    return alumne != null &&
                            alumne.getIdInstitut().equals(idInstitut);
                })
                .count();
    }
}