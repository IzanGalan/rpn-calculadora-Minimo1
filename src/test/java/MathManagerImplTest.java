import manager.MathManager;
import manager.MathManagerImpl;
import models.Alumne;
import models.Institut;
import models.Operacio;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class MathManagerImplTest {

    private MathManager mm;

    @Before
    public void setUp() throws Exception {
        mm = MathManagerImpl.getInstance();
        // Institut i alumne de prova
        mm.afegirInstitut(new Institut("I1", "IES Castelldefels"));
        mm.afegirAlumne(new Alumne("A1", "Joan", "Garcia", "I1"));
    }

    @After
    public void tearDown() {
        mm.clear();
    }

    @Test
    public void testRequerirIProcessar() throws Exception {
        mm.requerirOperacio(new Operacio("OP1", "A1", "5 1 2 + 4 * + 3 -"));
        Assert.assertEquals(1, mm.numOperacionsPendents());

        Operacio op = mm.processarOperacio();
        Assert.assertEquals(14.0, op.getResultat(), 0.001);
        Assert.assertEquals("PROCESSED", op.getEstat());
        Assert.assertEquals(0, mm.numOperacionsPendents());
    }

    @Test
    public void testFIFO() throws Exception {
        // Les operacions es processen en ordre d'arribada
        mm.requerirOperacio(new Operacio("OP1", "A1", "3 4 +"));  // 7
        mm.requerirOperacio(new Operacio("OP2", "A1", "10 2 /")); // 5

        Operacio primera = mm.processarOperacio();
        Assert.assertEquals("OP1", primera.getIdOperacio());
        Assert.assertEquals(7.0, primera.getResultat(), 0.001);

        Operacio segona = mm.processarOperacio();
        Assert.assertEquals("OP2", segona.getIdOperacio());
        Assert.assertEquals(5.0, segona.getResultat(), 0.001);
    }

    @Test(expected = Exception.class)
    public void testProcessarSensePendents() throws Exception {
        mm.processarOperacio();
    }

    @Test(expected = Exception.class)
    public void testAlumneInstitutInexistent() throws Exception {
        mm.afegirAlumne(new Alumne("A2", "Maria", "Vila", "INEXISTENT"));
    }

    @Test
    public void testLlistatPerAlumne() throws Exception {
        mm.requerirOperacio(new Operacio("OP1", "A1", "3 4 +"));
        mm.requerirOperacio(new Operacio("OP2", "A1", "5 2 *"));

        List<Operacio> ops = mm.llistatOperacionsPerAlumne("A1");
        Assert.assertEquals(2, ops.size());
    }

    @Test
    public void testLlistatPerInstitut() throws Exception {
        mm.afegirAlumne(new Alumne("A2", "Maria", "Vila", "I1"));
        mm.requerirOperacio(new Operacio("OP1", "A1", "3 4 +"));
        mm.requerirOperacio(new Operacio("OP2", "A2", "5 2 *"));

        List<Operacio> ops = mm.llistatOperacionsPerInstitut("I1");
        Assert.assertEquals(2, ops.size());
    }

    @Test
    public void testRankingInstituts() throws Exception {
        mm.afegirInstitut(new Institut("I2", "IES Gava"));
        mm.afegirAlumne(new Alumne("A2", "Maria", "Vila", "I2"));

        // I1 té 3 ops, I2 té 1
        mm.requerirOperacio(new Operacio("OP1", "A1", "1 2 +"));
        mm.requerirOperacio(new Operacio("OP2", "A1", "3 4 +"));
        mm.requerirOperacio(new Operacio("OP3", "A1", "5 6 +"));
        mm.requerirOperacio(new Operacio("OP4", "A2", "7 8 +"));

        List<Institut> ranking = mm.llistatInstitutsPorOperacions();
        Assert.assertEquals("I1", ranking.get(0).getIdInstitut());
    }
}