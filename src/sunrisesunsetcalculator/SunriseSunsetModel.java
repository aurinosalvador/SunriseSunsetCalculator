package sunrisesunsetcalculator;

import java.util.Calendar;

/**
 *
 * @author Eduardo
 */
public class SunriseSunsetModel {

    private double lat;
    private double lng;
    private boolean daySaving;
    private double timezone;
    private Calendar sunrise;
    private Calendar sunset;
    private Calendar solarNoon;

    public SunriseSunsetModel() {
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public boolean isDaySaving() {
        return daySaving;
    }

    public void setDaySaving(boolean daySaving) {
        this.daySaving = daySaving;
    }

    public double getTimezone() {
        return timezone;
    }

    public void setTimezone(double timezone) {
        this.timezone = timezone;
    }

    public Calendar getSunrise() {
        return sunrise;
    }

    public void setSunrise(Calendar sunrise) {
        this.sunrise = sunrise;
    }

    public Calendar getSunset() {
        return sunset;
    }

    public void setSunset(Calendar sunset) {
        this.sunset = sunset;
    }

    public Calendar getSolarNoon() {
        return solarNoon;
    }

    public void setSolarNoon(Calendar solarNoon) {
        this.solarNoon = solarNoon;
    }

}
