package mbourdin.cinema_cours.service;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilitiesTest {

    @Test
    public void isPowerOfTwo() {
        assertTrue(Utilities.isPowerOfTwo(1));
        System.out.println("1 : ok");
        assertTrue(Utilities.isPowerOfTwo(2));
        System.out.println("2 : ok");
        assertFalse(Utilities.isPowerOfTwo(3));
        System.out.println("3 : ok");
        assertTrue(Utilities.isPowerOfTwo(1024));
        System.out.println("1024 : ok");
        assertFalse(Utilities.isPowerOfTwo(125));
        System.out.println("125 : ok");
    }
}