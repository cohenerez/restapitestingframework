package com.erez.restfull.apitestingframework.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;

import static org.testng.Assert.assertEquals;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import io.restassured.RestAssured;


public class IoHelper {

    String  path;

    public String loadProperties(String property) {
        Properties properties = new Properties();
        InputStream input;
        try {
            input = new FileInputStream(path);
            properties.load(input);
        } catch (Exception e) {
              e.printStackTrace();
        }

        return properties.getProperty(property);
    }

     public  IoHelper setPath(String thePath) {
        this.path = thePath;
        return this;
    }

    public static String[][] ReadTSV(String fileName) throws InterruptedException {

        String csvFile = fileName;

        List<String[]> listTwoDims = new ArrayList<String[]>();

        BufferedReader br = null;
        String line = "";
        String SplitBy = "\t";

        try {
               br = new BufferedReader(new FileReader(csvFile));
                br.readLine();
              while ((line = br.readLine()) != null) {
                 listTwoDims.add(line.split(SplitBy));
             }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        String[][] arrayTwoDims = new String[listTwoDims.size()][];
        for (int i = 0; i < listTwoDims.size(); i++) {

            arrayTwoDims[i] = listTwoDims.get(i);
        }

        return arrayTwoDims;
    }

    public static List<String[]> ReadCSV(String file) throws Exception {
        FileReader filereader = new FileReader(file);
        CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
        List<String[]> allData = csvReader.readAll();

        return allData;
    }

    public static void assertAll(String input, String output) {
        try {
            JSONAssert.assertEquals(input, output, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void assertAll(String input, String output, String strict) {
        try {
            JSONAssert.assertEquals(input, output, true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void assertValue(String input, String output) {
        assertEquals(input,output);
    }

    public static void assertString(String actual, String expected, String errorMessage)
    {
        assertEquals(actual,expected,errorMessage);
    }

    public static void assertInt(int actual, int expected, String errorMessage)
    {
        assertEquals(actual,expected,errorMessage);
    }


    public static void assertValue(String input, String key, String value) {

        try {
            JSONObject inputJson = new JSONObject(input);
            String Key = inputJson.get(key).toString();
            assertEquals(Key,value);
        } catch (JSONException e) {

        }

    }

    public static String getValue(String input, String array, int pos, String key) throws JSONException {

        JSONObject inputJson = new JSONObject(input);
        JSONArray jsonArray = inputJson.getJSONArray(array);
        JSONObject job = jsonArray.getJSONObject(pos);
        String Key = job.get(key).toString();
        return Key;

    }

    public static String getValue(String input, String key) throws JSONException {

        JSONObject inputJson = new JSONObject(input);
        String value = inputJson.get(key).toString();
        return value;
    }



    public static Map<String, List<String>> splitQuery(URL url) throws UnsupportedEncodingException {
        final Map<String, List<String>> queryPairs = new LinkedHashMap<String, List<String>>();
        final String[] pairs = url.getQuery().split("&");

        for (String pair : pairs) {
            final int idx = pair.indexOf("=");
            final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
            if (!queryPairs.containsKey(key)) {
                queryPairs.put(key, new LinkedList<String>());
            }
            final String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
            queryPairs.get(key).add(value);
        }
        return queryPairs;
    }


    public String getSiteToken() {

        String siteToken = null;
        IoHelper getHelp = new IoHelper();
        getHelp.setPath("src/main/resources/Constants.properties");
        try {
            siteToken = getHelp.loadProperties("SiteToken");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return siteToken;
    }

}
