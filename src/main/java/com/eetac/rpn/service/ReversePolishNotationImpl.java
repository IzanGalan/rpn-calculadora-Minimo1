package com.eetac.rpn.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Stack;

public class ReversePolishNotationImpl implements ReversePolishNotation {

    private static final Logger logger = LogManager.getLogger(ReversePolishNotationImpl.class);

    @Override
    public double process(String expressio) throws Exception {
        logger.info("process - inici - expressio={}", expressio);

        if (expressio == null || expressio.trim().isEmpty()) {
            logger.error("process - expressio buida o nul·la");
            throw new Exception("L'expressió no pot ser buida");
        }

        Stack<Double> pila = new Stack<>();
        String[] tokens = expressio.trim().split("\\s+");

        for (String token : tokens) {
            if (esOperador(token)) {
                if (pila.size() < 2) {
                    logger.error("process - no hi ha suficients operands per {}", token);
                    throw new Exception("Expressió invàlida");
                }

                double segon = pila.pop();
                double primer = pila.pop();
                double resultat = calcular(primer, segon, token);

                pila.push(resultat);
                logger.info("process - operacio {} {} {} = {}", primer, token, segon, resultat);
            } else {
                try {
                    pila.push(Double.parseDouble(token));
                } catch (NumberFormatException e) {
                    logger.error("process - token invàlid {}", token);
                    throw new Exception("Token invàlid: " + token);
                }
            }
        }

        if (pila.size() != 1) {
            logger.error("process - expressio mal formada, pila={}", pila);
            throw new Exception("Expressió mal formada");
        }

        double resultatFinal = pila.pop();
        logger.info("process - fi - resultat={}", resultatFinal);
        return resultatFinal;
    }

    private boolean esOperador(String token) {
        return "+".equals(token) || "-".equals(token) || "*".equals(token) || "/".equals(token);
    }

    private double calcular(double primer, double segon, String operador) throws Exception {
        switch (operador) {
            case "+":
                return primer + segon;
            case "-":
                return primer - segon;
            case "*":
                return primer * segon;
            case "/":
                if (segon == 0) {
                    logger.fatal("calcular - divisio per zero");
                    throw new Exception("Divisió per zero");
                }
                return primer / segon;
            default:
                throw new Exception("Operador desconegut: " + operador);
        }
    }
}