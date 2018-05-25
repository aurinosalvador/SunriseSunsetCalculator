package sunrisesunsetcalculator;

import java.util.Calendar;

/**
 *
 * @author Aurino
 */
public class SunriseSunsetCalculator {

    private static double calcJD(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);

        if (month <= 2) {
            year -= 1;
            month += 12;
        }
        double a = Math.floor(year / 100);
        double b = 2 - a + Math.floor(a / 4);

        double jd = Math.floor(365.25 * (year + 4716)) + Math.floor(30.6001 * (month + 1)) + day + b - 1524.5;
        return jd;
    }

    private static double calcTimeJulianCent(double jd) {
        double t = (jd - 2451545.0) / 36525.0;
        return t;
    }

    private static double calcMeanObliquityOfEcliptic(double t) {
        double seconds = 21.448 - t * (46.8150 + t * (0.00059 - t * (0.001813)));
        double e0 = 23.0 + (26.0 + (seconds / 60.0)) / 60.0;
        return e0;	// in degrees
    }

    private static double calcObliquityCorrection(double t) {
        double e0 = calcMeanObliquityOfEcliptic(t);
        double omega = 125.04 - 1934.136 * t;
        double e = e0 + 0.00256 * Math.cos(Math.toRadians(omega));
        return e;
    }

    private static double calcGeomMeanLongSun(double t) {
        double l0 = 280.46646 + t * (36000.76983 + 0.0003032 * t);
        while (l0 > 360.0) {
            l0 -= 360.0;
        }
        while (l0 < 0.0) {
            l0 += 360.0;
        }
        return l0;
    }

    private static double calcGeomMeanAnomalySun(double t) {
        double m = 357.52911 + t * (35999.05029 - 0.0001537 * t);
        return m;
    }

    private static double calcSunEqOfCenter(double t) {
        double m = calcGeomMeanAnomalySun(t);
        double mrad = Math.toRadians(m);
        double sinm = Math.sin(mrad);
        double sin2m = Math.sin(mrad + mrad);
        double sin3m = Math.sin(mrad + mrad + mrad);

        double c = sinm * (1.914602 - t * (0.004817 + 0.000014 * t)) + sin2m * (0.019993 - 0.000101 * t) + sin3m * 0.000289;
        return c;		// in degrees
    }

    private static double calcSunTrueLong(double t) {
        double l0 = calcGeomMeanLongSun(t);
        double c = calcSunEqOfCenter(t);
        double o = l0 + c;
        return o;
    }

    private static double calcSunApparentLong(double t) {
        double o = calcSunTrueLong(t);
        double omega = 125.04 - 1934.136 * t;
        double lambda = o - 0.00569 - 0.00478 * Math.sin(Math.toRadians(omega));
        return lambda;		// in degrees
    }

    private static double calcSunDeclination(double t) {
        double e = calcObliquityCorrection(t);
        double lambda = calcSunApparentLong(t);

        double sint = Math.sin(Math.toRadians(e)) * Math.sin(Math.toRadians(lambda));
        double theta = Math.toDegrees(Math.asin(sint));
        return theta;		// in degrees
    }

    private static double calcEccentricityEarthOrbit(double t) {
        double e = 0.016708634 - t * (0.000042037 + 0.0000001267 * t);
        return e;		// unitless
    }

    private static double calcEquationOfTime(double t) {
        double epsilon = calcObliquityCorrection(t);
        double l0 = calcGeomMeanLongSun(t);
        double e = calcEccentricityEarthOrbit(t);
        double m = calcGeomMeanAnomalySun(t);

        double y = Math.tan(Math.toRadians(epsilon) / 2.0);
        y *= y;

        double sin2l0 = Math.sin(2.0 * Math.toRadians(l0));
        double sinm = Math.sin(Math.toRadians(m));
        double cos2l0 = Math.cos(2.0 * Math.toRadians(l0));
        double sin4l0 = Math.sin(4.0 * Math.toRadians(l0));
        double sin2m = Math.sin(2.0 * Math.toRadians(m));

        double etime = y * sin2l0 - 2.0 * e * sinm + 4.0 * e * y * sinm * cos2l0
                - 0.5 * y * y * sin4l0 - 1.25 * e * e * sin2m;

        return Math.toDegrees(etime) * 4.0;	// in minutes of time
    }

    private static double calcJDFromJulianCent(double t) {
        double jd = t * 36525.0 + 2451545.0;
        return jd;
    }

    private static double calcSolNoonUTC(double t, double longitude) {
        double tnoon = calcTimeJulianCent(calcJDFromJulianCent(t) + longitude / 360.0);
        double eqTime = calcEquationOfTime(tnoon);
        double solNoonUTC = 720 + (longitude * 4) - eqTime; // min
        double newt = calcTimeJulianCent(calcJDFromJulianCent(t) - 0.5 + solNoonUTC / 1440.0);
        eqTime = calcEquationOfTime(newt);
        solNoonUTC = 720 + (longitude * 4) - eqTime; // min
        return solNoonUTC;
    }

    private static double calcHourAngleSunrise(double latitude, double solarDec) {
        double latRad = Math.toRadians(latitude);
        double sdRad = Math.toRadians(solarDec);
        return (Math.acos(Math.cos(Math.toRadians(90.833))
                / (Math.cos(latRad) * Math.cos(sdRad))
                - Math.tan(latRad) * Math.tan(sdRad)));
    }

    private static double calcSunriseUTC(double jd, double latitude, double longitude) {
        double t = calcTimeJulianCent(jd);
        double noonmin = calcSolNoonUTC(t, longitude);
        double tnoon = calcTimeJulianCent(jd + noonmin / 1440.0);
        double eqTime = calcEquationOfTime(tnoon);
        double solarDec = calcSunDeclination(tnoon);
        double hourAngle = calcHourAngleSunrise(latitude, solarDec);
        double delta = longitude - Math.toDegrees(hourAngle);
        double timeDiff = 4 * delta;	// in minutes of time
        double timeUTC = 720 + timeDiff - eqTime;	// in minutes
        double newt = calcTimeJulianCent(calcJDFromJulianCent(t) + timeUTC / 1440.0);
        eqTime = calcEquationOfTime(newt);
        solarDec = calcSunDeclination(newt);
        hourAngle = calcHourAngleSunrise(latitude, solarDec);
        delta = longitude - Math.toDegrees(hourAngle);
        timeDiff = 4 * delta;
        timeUTC = 720 + timeDiff - eqTime; // in minutes
        return timeUTC;
    }

    private static boolean isNumber(double inputVal) {
        boolean oneDecimal = false;
        String inputStr = "" + inputVal;
        for (int i = 0; i < inputStr.length(); i++) {
            char oneChar = inputStr.charAt(i);
            if (i == 0 && (oneChar == '-' || oneChar == '+')) {
                continue;
            }
            if (oneChar == '.' && !oneDecimal) {
                oneDecimal = true;
                continue;
            }
            if (oneChar < '0' || oneChar > '9') {
                return false;
            }
        }
        return true;
    }

    private static double calcHourAngleSunset(double latitude, double solarDec) {
        double latRad = Math.toRadians(latitude);
        double sdRad = Math.toRadians(solarDec);
        return -(Math.acos(Math.cos(Math.toRadians(90.833)) / (Math.cos(latRad) * Math.cos(sdRad)) - Math.tan(latRad) * Math.tan(sdRad)));
    }

    private static double calcSunsetUTC(double jd, double latitude, double longitude) {
        double t = calcTimeJulianCent(jd);
        double noonmin = calcSolNoonUTC(t, longitude);
        double tnoon = calcTimeJulianCent(jd + noonmin / 1440.0);
        double eqTime = calcEquationOfTime(tnoon);
        double solarDec = calcSunDeclination(tnoon);
        double hourAngle = calcHourAngleSunset(latitude, solarDec);
        double delta = longitude - Math.toDegrees(hourAngle);
        double timeDiff = 4 * delta;
        double timeUTC = 720 + timeDiff - eqTime;
        double newt = calcTimeJulianCent(calcJDFromJulianCent(t) + timeUTC / 1440.0);
        eqTime = calcEquationOfTime(newt);
        solarDec = calcSunDeclination(newt);
        hourAngle = calcHourAngleSunset(latitude, solarDec);
        delta = longitude - Math.toDegrees(hourAngle);
        timeDiff = 4 * delta;
        timeUTC = 720 + timeDiff - eqTime; // in minutes
        return timeUTC;
    }

    private static void timeStringShortAMPM(Calendar calendar, double minutes) {
        double floatHour = minutes / 60.0;
        int hour = (int) Math.floor(floatHour);
        double floatMinute = 60.0 * (floatHour - Math.floor(floatHour));
        int minute = (int) Math.floor(floatMinute);
        double floatSec = 60.0 * (floatMinute - Math.floor(floatMinute));
        int second = (int) Math.floor(floatSec + 0.5);

        minute += (second >= 30) ? 1 : 0;

        if (minute >= 60) {
            minute -= 60;
            hour++;
        }

        if (hour > 23) {
            hour -= 24;
        }

        if (hour < 0) {
            hour += 24;
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
    }

    public static SunriseSunsetModel calcSun(Calendar calendar, double lat,
            double lng, boolean daySaving, double timeZone) {

        SunriseSunsetModel model = new SunriseSunsetModel();

        model.setLat(lat);
        model.setLng(lng);
        model.setDaySaving(daySaving);
        model.setTimezone(timeZone);
        model.setSunrise((Calendar) calendar.clone());
        model.setSunset((Calendar) calendar.clone());
        model.setSolarNoon((Calendar) calendar.clone());

        if ((lat >= -90) && (lat < -89)) {
            System.out.println("Toda latitude 89 e 90 S será igual -89");
            lat = -89;
        }

        if ((lat <= 90) && (lat > 89)) {
            System.out.println("Toda latitude 89 e 90 N será igual -89");
            lat = 89;
        }

        double jd = calcJD(calendar);
        double t = calcTimeJulianCent(jd);
        Boolean nosunrise = false;

        double riseTimeGMT = calcSunriseUTC(jd, lat, lng);
        if (!isNumber(riseTimeGMT)) {
            nosunrise = true;
        }

        // Calculate sunset for this date
        // if no sunset is found, set flag nosunset
        boolean nosunset = false;
        double setTimeGMT = calcSunsetUTC(jd, lat, lng);
        if (!isNumber(setTimeGMT)) {
            nosunset = true;
        }

        int daySavings = (daySaving ? 60 : 0);
        double zone = timeZone;
        if (zone > 12 || zone < -12.5) {
            zone = 0; // se zona tiver nesse intervalo será zerada
        }

        if (!nosunrise) { // Sunrise was found
            double riseTimeLocal = riseTimeGMT - (60 * zone) + daySavings;
            timeStringShortAMPM(model.getSunrise(), riseTimeLocal);
        }

        if (!nosunset) {
            double setTimeLocal = setTimeGMT - (60 * zone) + daySavings;
            timeStringShortAMPM(model.getSunset(), setTimeLocal);
        }

        // Calculate solar noon for this date
        double solNoonGMT = calcSolNoonUTC(t, lng);
        double solNoonLocal = solNoonGMT - (60 * zone) + daySavings;

        timeStringShortAMPM(model.getSolarNoon(), solNoonLocal);

        return model;
    }

}
