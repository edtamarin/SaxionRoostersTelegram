/**
 * Created by Egor Tamarin on 30-Mar-17.
 */
import org.json.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class RoosterAPI {
    public RoosterAPI(){}

    public ArrayList<String> search(String query) throws Exception{ // searches
        ArrayList<String> searchResult = new ArrayList<>(); // contains result
        String url = "http://api.roosters.saxion.nl/v2/search.json?q=" + query; // this sends a GET HTTP
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close(); // response received, proceed
        String servResponse =response.toString(); // parsing JSON
        JSONObject reply =new JSONObject(servResponse);
        int[] resultCount = new int[3];
        resultCount[0] = reply.getJSONObject("result").getJSONObject("count").getInt("total");
        resultCount[1] = reply.getJSONObject("result").getJSONObject("count").getInt("students");
        resultCount[2] = reply.getJSONObject("result").getJSONObject("count").getInt("teachers");
        if (!(resultCount[0]==0)){ // if there are results
            if (!(resultCount[1]==0)){ // students
                for (int i=0; i<resultCount[1];i++) {
                    JSONArray studentsFound;
                    studentsFound = reply.getJSONObject("result").getJSONArray("students");
                    searchResult.add(studentsFound.getJSONObject(i).getString("student")+
                            " " + studentsFound.getJSONObject(i).getString("group"));
                }
            }
            if (!(resultCount[2]==0)){ // teachers
                for (int i=0; i<resultCount[2];i++) {
                    JSONArray teachersFound;
                    teachersFound = reply.getJSONObject("result").getJSONArray("teachers");
                    searchResult.add(teachersFound.getJSONObject(i).getString("name")+
                            " " + teachersFound.getJSONObject(i).getString("code"));
                }
            }
        }else {
            searchResult.add("I can't find that. Try again, or go to the Student desk. I am not omniscient!");
        }
        return searchResult;
    }

    public ArrayList<String> schedule(String query) throws Exception{
        ArrayList<String> searchResult = new ArrayList<>(); // contains result
        String url = "http://api.roosters.saxion.nl/v2/schedule.json?group=" + query; // this sends a GET HTTP
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // optional default is GET
        con.setRequestMethod("GET");
        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close(); // response received, proceed
        String servResponse =response.toString(); // parsing JSON
        JSONObject reply = new JSONObject(servResponse);
        String courseName = reply.getJSONObject("subject").getJSONObject("group").getString("name");
        String week = reply.getJSONObject("week").getString("qweek");
        searchResult.add("Week "+ week + " for " + courseName + " looks like this:\n");
        JSONArray weekSchedule = reply.getJSONArray("days");
        int test = weekSchedule.length();
        for (int i = 0;i<weekSchedule.length();i++){
            searchResult.add(weekSchedule.getJSONObject(i).getJSONObject("date").getString("day") + ":\n");
            JSONArray daySchedule = weekSchedule.getJSONObject(i).getJSONArray("entries");
            if ((daySchedule.length()>0)) {
                for (int j = 0; j < daySchedule.length(); j++) {
                    searchResult.add(daySchedule.getJSONObject(j).getString("start") + " - " +
                            daySchedule.getJSONObject(j).getString("end") + "\n" +
                            daySchedule.getJSONObject(j).getString("name_en") + "\n" +
                            daySchedule.getJSONObject(j).getString("teachername") + "\n" +
                            daySchedule.getJSONObject(j).getString("room") + "\n");
                }
            }else{
                searchResult.add("No lectures!\n");
            }
        }
        return searchResult;
    }
}
