package steffen.utils;

import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class HeadingTest extends TestCase {
    @Test
    public void zeroDegreesShouldBeNorth() {
        assertTrue(Heading.NORTH.containsDirection(0));
    }

    @Test
    public void negative10degreesShoulBeNorth() {
        assertTrue(Heading.NORTH.containsDirection(-10));
    }

    @Test
    public void tenDegreesShoulBeNorth() {
        assertTrue(Heading.NORTH.containsDirection(10));
    }

    @Test
    public void positive350degreesShoulBeNorth() {
        assertTrue(Heading.NORTH.containsDirection(350));
    }

    @Test
    public void positive45degreesShouldNotBeNorth() {
        assertFalse(Heading.NORTH.containsDirection(45));
    }

    @Test
    public void positive45degreesShouldBeNorthEast() {
        assertTrue(Heading.NORTH_EAST.containsDirection(45));
    }

    @Test
    public void shouldReturnNorthEastFor45Degrees() {
        assertEquals(Heading.getHeadingForAngle(45), Heading.NORTH_EAST);
    }

    @Test
    public void shouldNotReturnNorthEastFor180Degrees() {
        assertNotEquals(Heading.getHeadingForAngle(180), Heading.NORTH_EAST);
    }
}
