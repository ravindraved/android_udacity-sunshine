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

    public static void main(String[] args) {

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
                InputStream inputStream = null;
                StringBuffer stringBuffer = new StringBuffer();
                if (inputStream == null) {
                    //do nothing
                    forecastJsonStr = null;
                    System.out.println("NullA!!");

                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                System.out.println("DONE!!!");


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }


        }

    }
}

