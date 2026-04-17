package com.eetac.rpn;

import com.eetac.rpn.service.ReversePolishNotation;
import com.eetac.rpn.service.ReversePolishNotationImpl;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReversePolishNotationImplTest {

    private ReversePolishNotation rpn;

    @BeforeEach
    void setUp() {
        rpn = new ReversePolishNotationImpl();
    }

    // Test de l'exemple del enunciat: 5 1 2 + 4 * + 3 - = 14
    @Test
    @Order(1)
    void testExempleEnunciat() throws Exception {
        double resultat = rpn.process("5 1 2 + 4 * + 3 -");
        assertEquals(14.0, resultat, 0.001);
    }

    // Test suma simple: 3 4 + = 7
    @Test
    @Order(2)
    void testSuma() throws Exception {
        assertEquals(7.0, rpn.process("3 4 +"), 0.001);
    }

    // Test resta: 10 3 - = 7
    @Test
    @Order(3)
    void testResta() throws Exception {
        assertEquals(7.0, rpn.process("10 3 -"), 0.001);
    }

    // Test multiplicació: 3 4 * = 12
    @Test
    @Order(4)
    void testMultiplicacio() throws Exception {
        assertEquals(12.0, rpn.process("3 4 *"), 0.001);
    }

    // Test divisió: 10 2 / = 5
    @Test
    @Order(5)
    void testDivisio() throws Exception {
        assertEquals(5.0, rpn.process("10 2 /"), 0.001);
    }

    // Test divisió per zero → excepció
    @Test
    @Order(6)
    void testDivisioPerZero() {
        Exception ex = assertThrows(Exception.class,
                () -> rpn.process("5 0 /"));
        assertTrue(ex.getMessage().contains("zero"));
    }

    // Test expressió buida → excepció
    @Test
    @Order(7)
    void testExpressioBuilda() {
        assertThrows(Exception.class, () -> rpn.process(""));
    }

    // Test token invàlid → excepció
    @Test
    @Order(8)
    void testTokenInvalid() {
        assertThrows(Exception.class, () -> rpn.process("3 abc +"));
    }

    // Test expressió mal formada → excepció
    @Test
    @Order(9)
    void testExpressioMalFormada() {
        // Massa operands, cap operador al final
        assertThrows(Exception.class, () -> rpn.process("3 4 5"));
    }

    // Test número negatiu
    @Test
    @Order(10)
    void testNumeroNegatiu() throws Exception {
        // -3 + 5 = 2  →  -3 5 +
        assertEquals(2.0, rpn.process("-3 5 +"), 0.001);
    }
}