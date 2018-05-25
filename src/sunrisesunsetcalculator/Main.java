package sunrisesunsetcalculator;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author Aurino
 */
public class Main {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        SunriseSunsetCalculator calc = new SunriseSunsetCalculator();
        
        GregorianCalendar calendar = new GregorianCalendar(2018, 3, 3);
        
        double latitude = -22.160271;
        double longitude = -42.421823;
        boolean daySaving = false;
        int timeZone = -3;

        SunriseSunsetModel model = SunriseSunsetCalculator.calcSun(calendar, latitude, longitude, daySaving, timeZone);

        System.out.println("Sunrise: " + sdf.format(model.getSunrise().getTime()));
        System.out.println("Sunset: " + sdf.format(model.getSunset().getTime()));
        System.out.println("Solar Noon: " + sdf.format(model.getSolarNoon().getTime()));
    }

}
