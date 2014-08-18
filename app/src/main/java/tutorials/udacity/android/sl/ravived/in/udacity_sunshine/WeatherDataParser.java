package tutorials.udacity.android.sl.ravived.in.udacity_sunshine;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by rved on 16/08/14.
 */
public class WeatherDataParser {


    private final static String LOG_TAG = WeatherDataParser.class.getSimpleName();

    /**
     * Given a string of the form returned by the api call:
     * http://api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7
     * retrieve the maximum temperature for the day indicated by dayIndex
     * (Note: 0-indexed, so 0 would refer to the first day).
     */
    public static double getMaxTemperatureForDay(String weatherJsonStr, int dayIndex)
            throws Exception {
        // TODO: add parsing code here

        double mxTemp = test2(weatherJsonStr, dayIndex);
        Log.d(LOG_TAG, mxTemp + "");

        return -1;
    }

    public static double test2(String weatherJsonStr, int dayIndex) throws Exception {

        JSONObject jsonObject = new JSONObject(weatherJsonStr);
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        JSONObject indexDayJsonObject = jsonArray.getJSONObject(dayIndex);
        JSONObject tempObjectForIndex = (JSONObject) indexDayJsonObject.get("temp");
        Log.d(LOG_TAG, tempObjectForIndex.get("min").toString());
        Log.d(LOG_TAG, tempObjectForIndex.get("max").toString());
        double mxTemp = Double.parseDouble(tempObjectForIndex.get("max") + "");
        return mxTemp;
    }


    public static void test1(String weatherJsonStr, int dayIndex) throws Exception {

        Log.d(LOG_TAG, "parsing JSON..");

        JSONObject jsonObject = new JSONObject(weatherJsonStr);

        Log.d(LOG_TAG, jsonObject.names().toString());
        JSONArray jsonArray = jsonObject.getJSONArray("list");

        Log.d(LOG_TAG, jsonArray.get(dayIndex).toString());


        // JSONArray dayArray = (JSONArray) jsonArray.get(dayIndex);
        Log.d(LOG_TAG, "convert jsonArray to JSONObject..");
        JSONObject indexDayJsonObject = jsonArray.getJSONObject(dayIndex);

        Log.d(LOG_TAG, indexDayJsonObject.names().toString());
        JSONArray indexDayJsonArray = indexDayJsonObject.getJSONArray("weather");
        Log.d(LOG_TAG, indexDayJsonArray.toString().toString());
        JSONObject tempObjectForIndex = (JSONObject) indexDayJsonObject.get("temp");
        Log.d(LOG_TAG, tempObjectForIndex.get("min").toString());
        Log.d(LOG_TAG, tempObjectForIndex.get("max").toString());


    }


    /* The date/time conversion code is going to be moved outside the asynctask later,
     * so for convenience we're breaking it out into its own method now.
     */
    private String getReadableDateString(long time) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        Date date = new Date(time * 1000);
        SimpleDateFormat format = new SimpleDateFormat("E, MMM d");
        return format.format(date).toString();
    }

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {
        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }

    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     * <p/>
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    public String[] getWeatherDataFromJson(String forecastJsonStr, int numDays)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "list";
        final String OWM_WEATHER = "weather";
        final String OWM_TEMPERATURE = "temp";
        final String OWM_MAX = "max";
        final String OWM_MIN = "min";
        final String OWM_DATETIME = "dt";
        final String OWM_DESCRIPTION = "main";

        Log.d(LOG_TAG, " Parsing JSON data to Object..");

        JSONObject forecastJson = new JSONObject(forecastJsonStr);
        JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

        String[] resultStrs = new String[numDays];
        for (int i = 0; i < weatherArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

            // Get the JSON object representing the day
            JSONObject dayForecast = weatherArray.getJSONObject(i);

            // The date/time is returned as a long.  We need to convert that
            // into something human-readable, since most people won't read "1400356800" as
            // "this saturday".
            long dateTime = dayForecast.getLong(OWM_DATETIME);
            day = getReadableDateString(dateTime);

            // description is in a child array called "weather", which is 1 element long.
            JSONObject weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
            description = weatherObject.getString(OWM_DESCRIPTION);

            // Temperatures are in a child object called "temp".  Try not to name variables
            // "temp" when working with temperature.  It confuses everybody.
            JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
            double high = temperatureObject.getDouble(OWM_MAX);
            double low = temperatureObject.getDouble(OWM_MIN);

            highAndLow = formatHighLows(high, low);
            resultStrs[i] = day + " - " + description + " - " + highAndLow;
        }

        for (String s : resultStrs) {
            Log.v(LOG_TAG, "Forecast Entry: " + s);
        }

        return resultStrs;
    }


}
