/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sunrisesunsetcalculator;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 *
 * @author aurino
 */
public class Main {

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        SunriseSunsetCalculator calc = new SunriseSunsetCalculator();
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(2018, 4, 25);
        System.out.println(calc.calcSun(calendar, "-07.1247", "-39.1855", false, -3));


    }

}
