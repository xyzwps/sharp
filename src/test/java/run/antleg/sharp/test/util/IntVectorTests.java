package run.antleg.sharp.test.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IntVectorTests {

    @Test
    public void incrementSimple() {
        var v = new IntVector(new IntVector.Boundary(1, 3), 1, 1, 1);
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                for (int k = 1; k <= 3; k++) {
                    assertEquals(String.format("(%d,%d,%d)", i, j, k), v.toString());
                    v = v.increment();
                }
            }
        }
        assertEquals("(1,1,1)", v.toString());
    }

    @Test
    public void incrementHuge() {
        final int INF = -10, SUP = 10;
        var v = new IntVector(new IntVector.Boundary(INF, SUP), INF, INF, INF);
        for (int i = INF; i <= SUP; i++) {
            for (int j = INF; j <= SUP; j++) {
                for (int k = INF; k <= SUP; k++) {
                    assertEquals(String.format("(%d,%d,%d)", i, j, k), v.toString());
                    v = v.increment();
                }
            }
        }
        assertEquals("(-10,-10,-10)", v.toString());
    }
}
