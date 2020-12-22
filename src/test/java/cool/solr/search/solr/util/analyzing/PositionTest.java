package cool.solr.search.solr.util.analyzing;

import org.junit.Test;

import static org.junit.Assert.*;

public class PositionTest {

    @Test
    public void testEquals() throws Exception {
        Position startEnd1 = new Position(0, 1, 1);
        Position startEnd2 = new Position(2, 0, 1);
        Position startEnd3 = new Position(0, 3, 1);
        Position startEndEquals1 = new Position(0, 1, 1);

        assertEquals(startEnd1, startEndEquals1);
        assertNotEquals(startEnd1, startEnd2);
        assertNotEquals(startEnd2, null);
        assertNotEquals(startEnd3, startEnd1);
    }

    @Test
    public void testHashCode() throws Exception {
        Position startEnd1 = new Position(0, 1, 1);
        Position startEnd2 = new Position(2, 0, 1);
        Position startEnd3 = new Position(11, 22, 1);
        Position startEnd4 = new Position(112, 2, 1);
        Position startEndEquals1 = new Position(0, 1, 1);

        assertNotEquals(startEnd1.hashCode(), startEnd2.hashCode());
        assertNotEquals(startEnd3.hashCode(), startEnd4.hashCode());
        assertEquals(startEnd1.hashCode(), startEndEquals1.hashCode());
    }

    @Test
    public void testMatches() {
        Position first = new Position(4, 10, 1);
        Position second = new Position(9, 10, 2);
        Position third = new Position(4, 10, 3);

        assertTrue(first.matches(third));
        assertTrue(third.matches(first));
        assertFalse(first.matches(second));
        assertFalse(third.matches(second));
    }

    @Test
    public void testContains() {
        Position first = new Position(4, 10, 1);
        Position second = new Position(9, 10, 2);
        Position third = new Position(4, 10, 3);
        Position fourth = new Position(3, 10, 3);
        Position fifth = new Position(3, 11, 3);

        assertTrue(first.contains(second));
        assertTrue(first.contains(third));
        assertFalse(first.contains(fourth));
        assertFalse(first.contains(fifth));
        assertTrue(fifth.contains(first));
    }

}
