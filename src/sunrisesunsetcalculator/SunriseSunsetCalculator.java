package sunrisesunsetcalculator;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author aurino
 */
public class SunriseSunsetCalculator {
    
    private double alpha;
    
    //Converte a Latitude de graus para decimal
    public Double getLatitude(String lat){
        double decLat;
        Boolean neg = false;
        String strLatDeg = lat.substring(0, lat.indexOf("."));
        double degs = Double.valueOf(strLatDeg);
        
        if(lat.charAt(0) == '-'){
            neg = true;
        }
        
        double mins = Double.valueOf(lat.substring(lat.indexOf(".") + 1, lat.indexOf(".") + 3));
        double secs = Double.valueOf(lat.substring(lat.indexOf(".") + 3));
            
        if(!neg){
            decLat = degs + (mins / 60) + (secs / 3600);
        } else {
            decLat = degs - (mins / 60) - (secs / 3600);
        }
        return decLat;
    }
    
    //Converte a Longitude de graus para decimal
    public Double getLongitude(String longi){
        double decLongi;
        Boolean neg = false;
        String strLonDeg = longi.substring(0, longi.indexOf("."));
        double degs = Double.valueOf(strLonDeg);
        
        if(longi.charAt(0) == '-'){
            neg = true;
        }
        
        double mins = Double.valueOf(longi.substring(longi.indexOf(".") + 1, longi.indexOf(".") + 3));
        double secs = Double.valueOf(longi.substring(longi.indexOf(".") + 3));
            
        if(!neg){
            decLongi = degs + (mins / 60) + (secs / 3600);
        } else {
            decLongi = degs - (mins / 60) - (secs / 3600);
        }
        return decLongi;
    }
    
    // Convert radian angle to degrees

    public double radToDeg(double angleRad){
		return (180.0 * angleRad / Math.PI);
	}

    //*********************************************************************/

    // Convert degree angle to radians

    public double degToRad(double angleDeg){
		return (Math.PI * angleDeg / 180.0);
	}
    
    public double calcJD(GregorianCalendar calendar){
        int day = calendar.getTime().getDate();
        int month = calendar.getTime().getMonth() + 1;
        int year = calendar.getTime().getYear() + 1900;
        
        if(month <= 2){
            year -= 1;
            month += 12;
        }
        double a = Math.floor(year/100);
        double b = 2 - a + Math.floor(a/4);
        
        double jd = Math.floor(365.25*(year + 4716)) + Math.floor(30.6001*(month+1)) + day + b - 1524.5;
        return jd;
    }
    
    public String calcDayOfWeek(double juld){
        int  a = (int) (juld + 1.5) % 7;
        String dow = (a==0)?"Sunday":(a==1)?"Monday":(a==2)?"Tuesday":(a==3)?"Wednesday":(a==4)?"Thursday":(a==5)?"Friday":"Saturday";
        return dow;
    }
    
    public double calcDayOfYear(GregorianCalendar calendar, Boolean lpyr){
        int month = calendar.getTime().getMonth() + 1;
        int day = calendar.getTime().getDate();
        
        int k = (lpyr ? 1 : 2);
        double doy = Math.floor((275 * month)/9) - k * Math.floor((month + 9)/12) + day -30;
        
        return doy;
    }
    
    public double calcTimeJulianCent(double jd){
        double t = (jd - 2451545.0)/36525.0;
        return t;
    }
    
    public double calcMeanObliquityOfEcliptic(double t){
        double seconds = 21.448 - t*(46.8150 + t*(0.00059 - t*(0.001813)));
        double e0 = 23.0 + (26.0 + (seconds/60.0))/60.0;
	return e0;	// in degrees
    }
    
    public double calcObliquityCorrection(double t){
        double e0 = calcMeanObliquityOfEcliptic(t);
        double omega = 125.04 - 1934.136 * t;
        double e = e0 + 0.00256 * Math.cos(degToRad(omega));
        return e;
    }
    
    public double calcGeomMeanLongSun(double t){
        double l0 = 280.46646 + t * (36000.76983 + 0.0003032 * t);
        while(l0 > 360.0){
            l0 -= 360.0;
        }
	while(l0 < 0.0){
            l0 += 360.0;
	}
        return l0;
    }
    
    public double calcGeomMeanAnomalySun(double t){
        double m = 357.52911 + t * (35999.05029 - 0.0001537 * t);
        return m;
    }
    
    public double calcSunEqOfCenter(double t){
        double m = calcGeomMeanAnomalySun(t);
        double mrad = degToRad(m);
        double sinm = Math.sin(mrad);
        double sin2m = Math.sin(mrad+mrad);
        double sin3m = Math.sin(mrad+mrad+mrad);

        double c = sinm * (1.914602 - t * (0.004817 + 0.000014 * t)) + sin2m * (0.019993 - 0.000101 * t) + sin3m * 0.000289;
        return c;		// in degrees
    }
    
    public double calcSunTrueLong(double t){
        double l0 = calcGeomMeanLongSun(t);
        double c = calcSunEqOfCenter(t);
        double o = l0 + c;
        return o;
    }
    
    public double calcSunApparentLong(double t){
        double o = calcSunTrueLong(t);
        double omega = 125.04 - 1934.136 * t;
        double lambda = o - 0.00569 - 0.00478 * Math.sin(degToRad(omega));
	return lambda;		// in degrees
    }
    
    public double calcSunRtAscension(double t){
        double e = calcObliquityCorrection(t);
        double lambda = calcSunApparentLong(t);
        
        double tananum = (Math.cos(degToRad(e)) * Math.sin(degToRad(lambda)));
        double tanadenom = (Math.cos(degToRad(lambda)));
        double alpha = radToDeg(Math.atan2(tananum, tanadenom));
        return alpha;		// in degrees
    }
        
    public double calcSunDeclination(double t){
        double e = calcObliquityCorrection(t);
        double lambda = calcSunApparentLong(t);
        
        double sint = Math.sin(degToRad(e)) * Math.sin(degToRad(lambda));
        double theta = radToDeg(Math.asin(sint));
        return theta;		// in degrees
    }
    
    public double calcEccentricityEarthOrbit(double t){
        double e = 0.016708634 - t * (0.000042037 + 0.0000001267 * t);
	return e;		// unitless
    }
    
    public double calcEquationOfTime(double t){
        double epsilon = calcObliquityCorrection(t);
        double l0 = calcGeomMeanLongSun(t);
        double e = calcEccentricityEarthOrbit(t);
        double m = calcGeomMeanAnomalySun(t);

        double y = Math.tan(degToRad(epsilon)/2.0);
        y *= y;

        double sin2l0 = Math.sin(2.0 * degToRad(l0));
        double sinm   = Math.sin(degToRad(m));
        double cos2l0 = Math.cos(2.0 * degToRad(l0));
        double sin4l0 = Math.sin(4.0 * degToRad(l0));
        double sin2m  = Math.sin(2.0 * degToRad(m));

        double etime = y * sin2l0 - 2.0 * e * sinm + 4.0 * e * y * sinm * cos2l0
                        - 0.5 * y * y * sin4l0 - 1.25 * e * e * sin2m;

        return radToDeg(etime)*4.0;	// in minutes of time
    }
    
    public double calcJDFromJulianCent(double t){
        double jd = t * 36525.0 + 2451545.0;
	return jd;
    }
    
    public double calcSolNoonUTC(double t, double longitude){
        // First pass uses approximate solar noon to calculate eqtime
        double tnoon = calcTimeJulianCent(calcJDFromJulianCent(t) + longitude/360.0);
        //System.out.println(tnoon);
        double eqTime = calcEquationOfTime(tnoon);
        //System.out.println(eqTime);
        double solNoonUTC = 720 + (longitude * 4) - eqTime; // min
        //System.out.println(solNoonUTC);
        

        double newt = calcTimeJulianCent(calcJDFromJulianCent(t) -0.5 + solNoonUTC/1440.0); 

        eqTime = calcEquationOfTime(newt);
        // var solarNoonDec = calcSunDeclination(newt);
        solNoonUTC = 720 + (longitude * 4) - eqTime; // min

        return solNoonUTC;
    }
    
    public double calcHourAngleSunrise(double latitude, double solarDec){
        double latRad = degToRad(latitude);
        double sdRad  = degToRad(solarDec);
        
        double HAarg = (Math.cos(degToRad(90.833))/(Math.cos(latRad)*Math.cos(sdRad))-Math.tan(latRad) * Math.tan(sdRad));

        double HA = (Math.acos(Math.cos(degToRad(90.833))/(Math.cos(latRad)*Math.cos(sdRad))-Math.tan(latRad) * Math.tan(sdRad)));

        return HA;		// in radians
        
    }
    
    public double calcSunriseUTC(double jd, double latitude, double longitude){
        double t = calcTimeJulianCent(jd);
        // *** Find the time of solar noon at the location, and use
        //     that declination. This is better than start of the 
        //     Julian day
        double noonmin = calcSolNoonUTC(t, longitude);
        double tnoon = calcTimeJulianCent (jd +noonmin/1440.0);
        
        // *** First pass to approximate sunrise (using solar noon)
        double eqTime = calcEquationOfTime(tnoon);
        double solarDec = calcSunDeclination(tnoon);
        double hourAngle = calcHourAngleSunrise(latitude, solarDec);
        
        double delta = longitude - radToDeg(hourAngle);
        double timeDiff = 4 * delta;	// in minutes of time
        double timeUTC = 720 + timeDiff - eqTime;	// in minutes

        // alert("eqTime = " + eqTime + "\nsolarDec = " + solarDec + "\ntimeUTC = " + timeUTC);

        // *** Second pass includes fractional jday in gamma calc

        double newt = calcTimeJulianCent(calcJDFromJulianCent(t) + timeUTC/1440.0); 
        eqTime = calcEquationOfTime(newt);
        solarDec = calcSunDeclination(newt);
        hourAngle = calcHourAngleSunrise(latitude, solarDec);
        delta = longitude - radToDeg(hourAngle);
        timeDiff = 4 * delta;
        timeUTC = 720 + timeDiff - eqTime; // in minutes

        // alert("eqTime = " + eqTime + "\nsolarDec = " + solarDec + "\ntimeUTC = " + timeUTC);

        return timeUTC;
        
    }
        
    public Boolean isNumber(double inputVal){
        boolean oneDecimal = false;
		String inputStr = "" + inputVal;
		for (int i = 0; i < inputStr.length(); i++){
                    char oneChar = inputStr.charAt(i);
                    if (i == 0 && (oneChar == '-'|| oneChar == '+')){
                        continue;
                    }
                    if (oneChar == '.' && !oneDecimal){
                            oneDecimal = true;
                            continue;
                    }
                    if (oneChar < '0' || oneChar > '9'){
                            return false;
                    }
            }
            return true;
    }
    
    public double calcHourAngleSunset(double latitude, double solarDec){
        double latRad = degToRad(latitude);
        double sdRad  = degToRad(solarDec);

        double HAarg = (Math.cos(degToRad(90.833))/(Math.cos(latRad)*Math.cos(sdRad))-Math.tan(latRad) * Math.tan(sdRad));

        double HA = (Math.acos(Math.cos(degToRad(90.833))/(Math.cos(latRad)*Math.cos(sdRad))-Math.tan(latRad) * Math.tan(sdRad)));

        return -HA;		// in radians
    }
    
    public double calcSunsetUTC(double jd, double latitude, double longitude){
        double t = calcTimeJulianCent(jd);
        
        // *** Find the time of solar noon at the location, and use
        //     that declination. This is better than start of the 
        //     Julian day
        double noonmin = calcSolNoonUTC(t, longitude);
        double tnoon = calcTimeJulianCent (jd + noonmin/1440.0);
        
        // First calculates sunrise and approx length of day
        double eqTime = calcEquationOfTime(tnoon);
        double solarDec = calcSunDeclination(tnoon);
        double hourAngle = calcHourAngleSunset(latitude, solarDec);

        double delta = longitude - radToDeg(hourAngle);
        double timeDiff = 4 * delta;
        double timeUTC = 720 + timeDiff - eqTime;

        // first pass used to include fractional day in gamma calc

        double newt = calcTimeJulianCent(calcJDFromJulianCent(t) + timeUTC/1440.0); 
        eqTime = calcEquationOfTime(newt);
        solarDec = calcSunDeclination(newt);
        hourAngle = calcHourAngleSunset(latitude, solarDec);

        delta = longitude - radToDeg(hourAngle);
        timeDiff = 4 * delta;
        timeUTC = 720 + timeDiff - eqTime; // in minutes

        return timeUTC;
    }
    
    public String calcDayFromJD(double jd){
		double z = Math.floor(jd + 0.5);
		double f = (jd + 0.5) - z; 
                double A;
		if (z < 2299161) {
			A = z;
		} else {
			alpha = Math.floor((z - 1867216.25)/36524.25);
			A = z + 1 + alpha - Math.floor(alpha/4);
		}

		double B = A + 1524;
		double C = Math.floor((B - 122.1)/365.25);
		double D = Math.floor(365.25 * C);
		double E = Math.floor((B - D)/30.6001);

		double day = B - D - Math.floor(30.6001 * E) + f;
		double month = (E < 14) ? E - 1 : E - 13;
		double year = (month > 2) ? C - 4716 : C - 4715;

		//return ((day<10 ? "0" : "") + day + monthList[month-1].abbr);
                return ((day<10 ? "0" : "") + day + month);//TODO verificar se vai funcionar
	}
    
    public String timeStringShortAMPM(double minutes, double jd){
        double julianday = jd;
        double floatHour = minutes / 60.0;
        int hour = (int) Math.floor(floatHour);
        double floatMinute = 60.0 * (floatHour - Math.floor(floatHour));
        int  minute = (int) Math.floor(floatMinute);
        double floatSec = 60.0 * (floatMinute - Math.floor(floatMinute));
        int  second = (int) Math.floor(floatSec + 0.5);
        boolean PM = false;

        minute += (second >= 30)? 1 : 0;

        if (minute >= 60){
                minute -= 60;
                hour ++;
        }
        
        boolean daychange = false;
        if (hour > 23){
                hour -= 24;
                daychange = true;
                julianday += 1.0;
        }

        if (hour < 0){
                hour += 24;
                daychange = true;
                julianday -= 1.0;
        }

        if (hour > 12) {
                hour -= 12;
                PM = true;
        }

        if (hour == 12){
          PM = true;
        }

        if (hour == 0) {
                PM = false;
                hour = 12;
        }

        String timeStr = hour + ":";
        if (minute < 10)	//	i.e. only one digit
                timeStr += "0" + minute + ((PM)?"PM":"AM");
        else
                timeStr += "" + minute + ((PM)?"PM":"AM");

        if (daychange) return timeStr + " " + calcDayFromJD(julianday);
        return timeStr;

    }
    
    public String timeStringDate(double minutes, double jd){ 
        double julianday = jd;
        double floatHour = minutes / 60.0;
        int hour = (int) Math.floor(floatHour);
        double floatMinute = 60.0 * (floatHour - Math.floor(floatHour));
        int minute = (int) Math.floor(floatMinute);
        double floatSec = 60.0 * (floatMinute - Math.floor(floatMinute));
        int second = (int) Math.floor(floatSec + 0.5);

        minute += (second >= 30)? 1 : 0;

        if (minute >= 60){
                minute -= 60;
                hour ++;
        }

        boolean daychange = false;
        if (hour > 23) {
                hour -= 24;
                julianday += 1.0;
                daychange = true;
        }

        if (hour < 0){
                hour += 24;
                julianday -= 1.0;
                daychange = true;
        }

        String timeStr = hour + ":";
        if (minute < 10)	//	i.e. only one digit
                timeStr += "0" + minute;
        else
                timeStr += minute;

        if (daychange) return timeStr + " " + calcDayFromJD(julianday);
        return timeStr;
    }
    
    public String timeString(double minutes){
        double floatHour = minutes / 60.0;
        int hour = (int) Math.floor(floatHour);
        double floatMinute = 60.0 * (floatHour - Math.floor(floatHour));
        int minute = (int) Math.floor(floatMinute);
        double floatSec = 60.0 * (floatMinute - Math.floor(floatMinute));
        int second = (int) Math.floor(floatSec + 0.5);
        if (second > 59){
            second = 0;
            minute += 1;
        }
        
        String timeStr = hour + ":";
        if (minute < 10)	//	i.e. only one digit
            timeStr += "0" + minute + ":";
        else
            timeStr += minute + ":";
        if (second < 10)	//	i.e. only one digit
            timeStr += "0" + second;
        else
            timeStr += second;

        return timeStr;
    }
    
    public String calcSun(GregorianCalendar calendar, String lat, String longi, boolean daySave, double zoneTime){
        String result = null;
        double latitude = getLatitude(lat);
        double longitude = getLongitude(longi);
        boolean daySaving = daySave;
        //TODO - Implementar esse dado, por enquanto tá apenas em false
        //TODO - Implementar adição do TimeZone
        double timeZone = zoneTime;
        result = "Lat: " + latitude + "\nLong: " + longitude +
                "\nDaySaving? " + daySaving + "\nTimeZone: " + timeZone;
        
        if((latitude >= -90) && (latitude < -89)){
            System.out.println("Toda latitude 89 e 90 S será igual -89");
		latitude = -89;
        }
        
        if((latitude <= 90) && (latitude > 89)){
            System.out.println("Toda latitude 89 e 90 N será igual -89");
		latitude = 89;
        }
        
        
        double jd = calcJD(calendar);
        String dow = calcDayOfWeek(jd);
        double doy = calcDayOfYear(calendar, calendar.isLeapYear(calendar.getTime().getYear()));
        double t = calcTimeJulianCent(jd);
        alpha = calcSunRtAscension(t);
        double theta = calcSunDeclination(t);
        double etime = calcEquationOfTime(t);
        
        double eqTime = etime;
	double solarDec = theta;
        
        // Calculate sunrise for this date
	// if no sunrise is found, set flag nosunrise
        Boolean nosunrise = false;
        
        double riseTimeGMT = calcSunriseUTC(jd, latitude, longitude);
        if (!isNumber(riseTimeGMT)){
            nosunrise = true;
	}
        
        // Calculate sunset for this date
        // if no sunset is found, set flag nosunset

        boolean nosunset = false;
        double setTimeGMT = calcSunsetUTC(jd, latitude, longitude);
        if (!isNumber(setTimeGMT)){
                nosunset = true;
        }
        
        int daySavings = (daySaving ? 60 : 0);
        double zone = timeZone;
        if(zone > 12 || zone < -12.5){
            zone = 0; // se zona tiver nesse intervalo será zerada
        }
        
        if (!nosunrise){ // Sunrise was found
            double riseTimeLST = riseTimeGMT - (60 * zone) + daySavings;
            String riseStr = timeStringShortAMPM(riseTimeLST, jd);
            String utcRiseStr = timeStringDate(riseTimeGMT, jd);
            
            result += "\nSunrise Time: " + riseStr + " UTC: " + utcRiseStr;
        }
        
        if (!nosunset){
            double setTimeLST = setTimeGMT - (60 * zone) + daySavings;
            String setStr = timeStringShortAMPM(setTimeLST, jd);
            String utcSetStr = timeStringDate(setTimeGMT, jd);

            result += "\nSunset Time: " + setStr + " UTC: " + utcSetStr;
        }
        
        // Calculate solar noon for this date
        double solNoonGMT = calcSolNoonUTC(t, longitude);
        double solNoonLST = solNoonGMT - (60 * zone) + daySavings;

        String solnStr = timeString(solNoonLST);
        String utcSolnStr = timeString(solNoonGMT);
        
        return result += "\nSolar Noon: " + solnStr + " UTC: " + utcSolnStr;
        
    
    
    }
    
    
}
