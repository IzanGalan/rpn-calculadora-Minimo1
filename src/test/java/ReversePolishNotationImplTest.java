import manager.ReversePolishNotation;
import manager.ReversePolishNotationImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ReversePolishNotationImplTest {

    private ReversePolishNotation rpn;

    @Before
    public void setUp() {
        rpn = new ReversePolishNotationImpl();
    }

    @Test
    public void testExempleEnunciat() throws Exception {
        // 5+((1+2)*4)-3 = 14
        double res = rpn.process("5 1 2 + 4 * + 3 -");
        Assert.assertEquals(14.0, res, 0.001);
    }

    @Test
    public void testSuma() throws Exception {
        Assert.assertEquals(7.0, rpn.process("3 4 +"), 0.001);
    }

    @Test
    public void testResta() throws Exception {
        Assert.assertEquals(7.0, rpn.process("10 3 -"), 0.001);
    }

    @Test
    public void testMultiplicacio() throws Exception {
        Assert.assertEquals(12.0, rpn.process("3 4 *"), 0.001);
    }

    @Test
    public void testDivisio() throws Exception {
        Assert.assertEquals(5.0, rpn.process("10 2 /"), 0.001);
    }

    @Test(expected = Exception.class)
    public void testDivisioPerZero() throws Exception {
        rpn.process("5 0 /");
    }

    @Test(expected = Exception.class)
    public void testExpressioBuilda() throws Exception {
        rpn.process("");
    }

    @Test(expected = Exception.class)
    public void testTokenInvalid() throws Exception {
        rpn.process("3 abc +");
    }

    @Test(expected = Exception.class)
    public void testExpressioMalFormada() throws Exception {
        rpn.process("3 4 5"); // massa operands sense operador
    }
}