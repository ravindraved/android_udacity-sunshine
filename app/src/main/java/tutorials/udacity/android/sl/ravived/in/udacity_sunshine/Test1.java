package tutorials.udacity.android.sl.ravived.in.udacity_sunshine;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by rved on 25/07/14.
 */
public class Test1 {

    public static void main(String[] args) throws Exception {


        // Calling JSON object in standalone mode does not work.. gives RuntimeException: Stub! .looks like it needs framework code.

        String weatherJson = test1();
        System.out.println(weatherJson);
        WeatherDataParser.getMaxTemperatureForDay(weatherJson, 1);
    }


    public static String test1() {
        {

            // These two need to be declared outside the try/catch

            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;


            try {

                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?id=1275339&mode=json&units=metric&cnt=7");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if (inputStream == null) {
                    //do nothing
                    forecastJsonStr = null;
                    return null;

                }


                reader = new BufferedReader(new InputStreamReader(inputStream));
                System.out.println("Done reading weather data...");


                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    stringBuffer.append(line + "\n");
                }

                if (stringBuffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    forecastJsonStr = null;
                    return null;
                }

                forecastJsonStr = stringBuffer.toString();

                return forecastJsonStr;


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
            }


        }
    }

}

