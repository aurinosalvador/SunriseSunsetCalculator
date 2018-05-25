package sunrisesunsetcalculator;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Eduardo
 */
public class SunriseSunsetCalculatorTest {

    public SunriseSunsetCalculatorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCalcSun1() {
        Calendar calendar = new GregorianCalendar(2018, 4, 25);
        double lat = -22.160271;
        double lng = -42.421823;
        boolean daySaving = false;
        double timeZone = -3.0;

        Calendar sunrise = new GregorianCalendar(2018, 4, 25, 6, 39, 10);
        Calendar sunset = new GregorianCalendar(2018, 4, 25, 17, 35, 10);
        Calendar solarNoon = new GregorianCalendar(2018, 4, 25, 12, 07, 12);

        testCalcSun(calendar, lat, lng, daySaving, timeZone, sunrise, sunset,
                solarNoon);
    }

    @Test
    public void testCalcSun2() {
        Calendar calendar = new GregorianCalendar(2018, 3, 3);
        double lat = -22.160271;
        double lng = -42.421823;
        boolean daySaving = false;
        double timeZone = -3.0;

        Calendar sunrise = new GregorianCalendar(2018, 3, 3, 6, 19, 40);
        Calendar sunset = new GregorianCalendar(2018, 3, 3, 18, 8, 15);
        Calendar solarNoon = new GregorianCalendar(2018, 3, 3, 12, 14, 46);

        testCalcSun(calendar, lat, lng, daySaving, timeZone, sunrise, sunset,
                solarNoon);
    }

    private void testCalcSun(Calendar calendar, double lat, double lng,
            boolean daySaving, double timeZone, Calendar sunrise,
            Calendar sunset, Calendar solarNoon) {

        SunriseSunsetModel expResult = new SunriseSunsetModel();
        expResult.setLat(lat);
        expResult.setLng(lng);
        expResult.setDaySaving(daySaving);
        expResult.setTimezone(timeZone);
        expResult.setSunrise(sunrise);
        expResult.setSunset(sunset);
        expResult.setSolarNoon(solarNoon);

        SunriseSunsetModel result = SunriseSunsetCalculator.calcSun(calendar, lat, lng, daySaving, timeZone);

        System.out.print("lat");
        assertEquals(expResult.getLat(), result.getLat(), 0.0);
        System.out.println(" - OK");

        System.out.print("lng");
        assertEquals(expResult.getLng(), result.getLng(), 0.0);
        System.out.println(" - OK");

        System.out.print("Sunrise");
        assertEquals(expResult.getSunrise().getTimeInMillis(), result.getSunrise().getTimeInMillis(), 0.0);
        System.out.println(" - OK");

        System.out.print("Sunset");
        assertEquals(expResult.getSunset().getTimeInMillis(), result.getSunset().getTimeInMillis(), 0.0);
        System.out.println(" - OK");

        System.out.print("Solar Noon");
        assertEquals(expResult.getSolarNoon().getTimeInMillis(), result.getSolarNoon().getTimeInMillis(), 0.0);
        System.out.println(" - OK");
    }

}
