package com.userinterfaces.ontime;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;


/**
 * Created by Jennifer on 25/11/2014.
 */
public class RetrieveWeather {
    private String parseString = "";
    private String messageCode = "";
    private String messageTemp = "";
    private int woeID;
    private String cityName;
    private int conditionCode;

    //findlocation
    private final Context mContext;
    private double latitude;
    private double longitude;
    //private String cityName;

    public RetrieveWeather(Context context, double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.mContext = context;
        //this.cityName = city;
        new Connection().execute();
    }

    private class Connection extends AsyncTask {
        @Override
        protected Object doInBackground(Object... arg0) {
            findCity();
            searchWOEID();
            searchWeather();
            return null;
        }

        protected Object onPostExecute (int conditioncode){
            return conditioncode;
        }
    }

    public void searchWeather() {
        try {
            String urlBase = "http://weather.yahooapis.com/forecastrss?w=";
            String urlUnits = "&u=c";
            //String urlString = "http://weather.yahooapis.com/forecastrss?w=22664159&u=c&";    //base url
            String urlString = urlBase + this.woeID + urlUnits;
            System.out.println(urlString);
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            System.out.println(con.getResponseCode());

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();

            //change toString
            parseString = response.toString();
            System.out.println(parseString);

            //parse XML
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(parseString));

            String tagName = null;
            //String currentTag = null;

            int event = parser.getEventType();
            boolean isFirstDayForecast = true;
            while (event != XmlPullParser.END_DOCUMENT) {
                tagName = parser.getName();

                if (event == XmlPullParser.START_TAG) {
                    if (tagName.equals("yweather:condition")) {
                        int code = Integer.parseInt(parser.getAttributeValue(null, "code"));
                        int temp = Integer.parseInt(parser.getAttributeValue(null, "temp"));
                        String date = parser.getAttributeValue(null, "date");

                        System.out.println("The code is " + code);
                        System.out.println("The temperature is " + temp);
                        System.out.println("The date is " + date);
                        this.conditionCode = code;
                    }
                }
                event = parser.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void searchWOEID() {
        final String yahooPlaceApisBase = "http://query.yahooapis.com/v1/public/yql?q=select*from%20geo.places%20where%20text=";
        final String yahooapisFormat = "&format=xml";
        String queryString = yahooPlaceApisBase + "%22" + this.cityName + "%22" + yahooapisFormat;
        int parseWoeID = 0;

        try {
            URL url = new URL(queryString);
            System.out.println(queryString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();
            //System.out.println("Testing woied1");
            //System.out.println(con.getResponseCode());
            //System.out.println("Testing woied2");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            //System.out.println("Testing woied3");
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine + "\n");
            }
            in.close();

            // System.out.println("Testing woied4");
            String woeIDParse = response.toString();
            //System.out.println("Testing woied5");
            //System.out.println(woeIDParse);

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(new StringReader(woeIDParse));
            String tagName = null;
            //String currentTag = null;

            int event = parser.getEventType();
            //boolean isFirstDayForecast = true;
            while (event != XmlPullParser.END_DOCUMENT) {
                tagName = parser.getName();

                if (event == XmlPullParser.START_TAG) {
                    if (tagName.equals("woeid")) {
                        event = parser.next();
                        if (event == XmlPullParser.TEXT) {

                            parseWoeID = Integer.parseInt(parser.getText());
                            //System.out.println(parseWoeID);
                        }

                        //System.out.println("The woeid is " + code);
                        break; //to grab only the first instance of woeid for city
                    }

                }
                event = parser.next();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        this.woeID = parseWoeID;
    }

    public int getWoeID() {
        return this.woeID;
    }

    public String getCondition()
    {
        String condition = "";

        switch (conditionCode)
        {
            case 10:
            case 11:
            case 12:
            case 35:
            case 40:
                condition = "RAIN";
                break;
            case 5:
            case 6:
            case 7:
            case 14:
            case 15:
            case 16:
            case 17:
            case 41:
            case 46:
                condition = "SNOW";
                break;
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
                condition = "CLOUDY";
            default:
                break;
        }
        return condition;
    }

    //location stuff

    public void findCity()
    {
        Geocoder gcd = new Geocoder(mContext, Locale.getDefault());

        System.out.println("Testing1 city");
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(this.latitude, this.longitude, 1);
            System.out.println("Testing2 city");
            if (addresses.size() > 0) {
                //System.out.println("Testing3");
                //System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
                System.out.println("Testing4 " + cityName);
                cityName = "Toronto";
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public String getCityName()
    {
        return cityName;
    }
}