package com.eetac.rpn;

import com.eetac.rpn.model.Alumne;
import com.eetac.rpn.model.Institut;
import com.eetac.rpn.model.Operacio;
import com.eetac.rpn.service.MathManager;
import com.eetac.rpn.service.MathManagerImpl;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MathManagerImplTest {

    private MathManager manager;

    @BeforeEach
    void setUp() {
        MathManagerImpl.resetInstance();
        manager = MathManagerImpl.getInstance();
    }

    // ── Institut ─────────────────────────────────────────────────────
    @Test
    @Order(1)
    void testAfegirInstitut() {
        Institut i = new Institut("I1", "IES Castelldefels");
        assertDoesNotThrow(() -> manager.afegirInstitut(i));
    }

    // ── Alumne ───────────────────────────────────────────────────────
    @Test
    @Order(2)
    void testAfegirAlumne_ok() throws Exception {
        manager.afegirInstitut(new Institut("I1", "IES Castelldefels"));
        Alumne a = new Alumne("A1", "Joan", "Puig", "I1");
        assertDoesNotThrow(() -> manager.afegirAlumne(a));
    }

    @Test
    @Order(3)
    void testAfegirAlumne_institutNoExisteix() {
        Alumne a = new Alumne("A1", "Joan", "Puig", "INEXISTENT");
        assertThrows(Exception.class, () -> manager.afegirAlumne(a));
    }

    // ── Requerir operació ────────────────────────────────────────────
    @Test
    @Order(4)
    void testRequerirOperacio_ok() throws Exception {
        manager.afegirInstitut(new Institut("I1", "IES"));
        manager.afegirAlumne(new Alumne("A1", "Joan", "Puig", "I1"));
        Operacio op = new Operacio("OP1", "A1", "5 1 2 + 4 * + 3 -");
        assertDoesNotThrow(() -> manager.requerirOperacio(op));
    }

    @Test
    @Order(5)
    void testRequerirOperacio_alumneNoExisteix() {
        Operacio op = new Operacio("OP1", "INEXISTENT", "3 4 +");
        assertThrows(Exception.class, () -> manager.requerirOperacio(op));
    }

    // ── Processar operació ───────────────────────────────────────────
    @Test
    @Order(6)
    void testProcessarOperacio_ok() throws Exception {
        manager.afegirInstitut(new Institut("I1", "IES"));
        manager.afegirAlumne(new Alumne("A1", "Joan", "Puig", "I1"));
        manager.requerirOperacio(new Operacio("OP1", "A1", "5 1 2 + 4 * + 3 -"));

        Operacio processada = manager.processarOperacio();
        assertEquals(14.0, processada.getResultat(), 0.001);
        assertEquals("PROCESSED", processada.getEstat());
    }

    @Test
    @Order(7)
    void testProcessarOperacio_FIFO() throws Exception {
        // Les operacions s'han de processar en ordre d'arribada
        manager.afegirInstitut(new Institut("I1", "IES"));
        manager.afegirAlumne(new Alumne("A1", "Joan", "Puig", "I1"));

        manager.requerirOperacio(new Operacio("OP1", "A1", "3 4 +"));  // 7
        manager.requerirOperacio(new Operacio("OP2", "A1", "10 2 /")); // 5

        Operacio primera = manager.processarOperacio();
        assertEquals("OP1", primera.getId());
        assertEquals(7.0, primera.getResultat(), 0.001);

        Operacio segona = manager.processarOperacio();
        assertEquals("OP2", segona.getId());
        assertEquals(5.0, segona.getResultat(), 0.001);
    }

    @Test
    @Order(8)
    void testProcessarOperacio_sensePendents() {
        assertThrows(Exception.class, () -> manager.processarOperacio());
    }

    // ── Llistats ─────────────────────────────────────────────────────
    @Test
    @Order(9)
    void testLlistatOperacionsPerAlumne() throws Exception {
        manager.afegirInstitut(new Institut("I1", "IES"));
        manager.afegirAlumne(new Alumne("A1", "Joan", "Puig", "I1"));
        manager.requerirOperacio(new Operacio("OP1", "A1", "3 4 +"));
        manager.requerirOperacio(new Operacio("OP2", "A1", "5 2 *"));

        List<Operacio> ops = manager.llistatOperacionsPerAlumne("A1");
        assertEquals(2, ops.size());
    }

    @Test
    @Order(10)
    void testLlistatOperacionsPerInstitut() throws Exception {
        manager.afegirInstitut(new Institut("I1", "IES"));
        manager.afegirAlumne(new Alumne("A1", "Joan", "Puig", "I1"));
        manager.afegirAlumne(new Alumne("A2", "Maria", "Vila", "I1"));
        manager.requerirOperacio(new Operacio("OP1", "A1", "3 4 +"));
        manager.requerirOperacio(new Operacio("OP2", "A2", "5 2 *"));

        List<Operacio> ops = manager.llistatOperacionsPerInstitut("I1");
        assertEquals(2, ops.size());
    }

    @Test
    @Order(11)
    void testLlistatInstitutsPorOperacions() throws Exception {
        // I1 té 3 ops, I2 té 1 op → I1 primer
        manager.afegirInstitut(new Institut("I1", "IES A"));
        manager.afegirInstitut(new Institut("I2", "IES B"));
        manager.afegirAlumne(new Alumne("A1", "Joan", "Puig", "I1"));
        manager.afegirAlumne(new Alumne("A2", "Maria", "Vila", "I2"));

        manager.requerirOperacio(new Operacio("OP1", "A1", "1 2 +"));
        manager.requerirOperacio(new Operacio("OP2", "A1", "3 4 +"));
        manager.requerirOperacio(new Operacio("OP3", "A1", "5 6 +"));
        manager.requerirOperacio(new Operacio("OP4", "A2", "7 8 +"));

        List<Institut> ordenats = manager.llistatInstitutsPorOperacions();
        assertEquals("I1", ordenats.get(0).getId()); // I1 primer (3 ops)
        assertEquals("I2", ordenats.get(1).getId()); // I2 segon (1 op)
    }
}