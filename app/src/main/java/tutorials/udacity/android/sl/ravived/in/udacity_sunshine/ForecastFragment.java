package tutorials.udacity.android.sl.ravived.in.udacity_sunshine;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rved on 27/07/14.
 */
public class ForecastFragment extends Fragment {

    private final static String LOG_TAG = FetchWeatherTask.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //once view for the Fragment is created ..

        String[] weatherForecastList = {

                "Today - Cloudy - 28/32",
                "Tomorrow - Drizzle - 27/33",
                "Day After Tomorrow - Showering - 25/29"

        };

        List<String> weekForecast = new ArrayList<String>(Arrays.asList(weatherForecastList));


        ArrayAdapter<String> mForecastAdapter = new ArrayAdapter<String>(
                //current context
                getActivity(),
                // id of list item layout
                R.layout.list_item_forecast,
                //id of text view to populate layout
                R.id.list_item_forecast_textview,
                //Forecast Data
                weatherForecastList
        );

        ListView listView = (ListView) rootView.findViewById(
                R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);


        Log.d(ForecastFragment.class.getName(), "Weather Data Received" + weekForecast);


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forcastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here. the action bar will automitically handle clicks
        // on the home/up button, so long as you specify  a parent activity in AndroidManifest.xml
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            new FetchWeatherTask().execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchWeatherTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Log.d(LOG_TAG, "in background");
            String weatherData = getWeatherDataFromServer();
            return null;
        }
    }


    public static String getWeatherDataFromServer() {

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


        } catch (Exception e) {
            Log.e("ForecastFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            forecastJsonStr = null;
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, e.toString());
                }
            }
        }


        return null;
    }

}

