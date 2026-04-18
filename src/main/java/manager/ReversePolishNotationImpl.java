package manager;

import org.apache.log4j.Logger;
import java.util.Stack;

public class ReversePolishNotationImpl implements ReversePolishNotation {

    final static Logger logger = Logger.getLogger(ReversePolishNotationImpl.class.getName());

    @Override
    public double process(String expressio) throws Exception {
        logger.info("RPN process INICI - expressio: " + expressio);

        if (expressio == null || expressio.trim().isEmpty()) {
            logger.error("ERROR: expressió buida");
            throw new Exception("L'expressió no pot ser buida");
        }

        Stack<Double> pila = new Stack<>();
        String[] tokens = expressio.trim().split("\\s+");

        for (String token : tokens) {
            if (esOperador(token)) {
                if (pila.size() < 2) {
                    logger.error("ERROR: operands insuficients per a " + token);
                    throw new Exception("Operands insuficients per a: " + token);
                }
                double b = pila.pop();
                double a = pila.pop();
                double res = calcular(a, b, token);
                pila.push(res);
            } else {
                try {
                    pila.push(Double.parseDouble(token));
                } catch (NumberFormatException e) {
                    logger.error("ERROR: token invàlid: " + token);
                    throw new Exception("Token invàlid: " + token);
                }
            }
        }

        if (pila.size() != 1) {
            logger.error("ERROR: expressió mal formada, pila=" + pila);
            throw new Exception("Expressió mal formada");
        }

        double resultat = pila.pop();
        logger.info("RPN process FI - resultat: " + resultat);
        return resultat;
    }

    private boolean esOperador(String token) {
        return token.equals("+") || token.equals("-")
                || token.equals("*") || token.equals("/");
    }

    private double calcular(double a, double b, String op) throws Exception {
        switch (op) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/":
                if (b == 0) {
                    logger.fatal("FATAL: divisió per zero");
                    throw new Exception("Divisió per zero");
                }
                return a / b;
            default:
                throw new Exception("Operador desconegut: " + op);
        }
    }
}