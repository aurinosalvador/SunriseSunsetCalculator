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
        calendar.getTime();
        String lat = "-07.1247";
        String longi = "-39.1855";
        boolean daySaving = false;
        int timeZone = -3;
        System.out.println(calc.calcSun(calendar, lat , longi, daySaving, timeZone));


    }

}
