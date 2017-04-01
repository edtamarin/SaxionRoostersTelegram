/**
 * Created by Egor Tamarin on 30-Mar-17.
 * Handles JSON replies.
 */
import org.json.*;

import java.util.ArrayList;

public class RoosterAPI {
    private String[] daysEN = {"MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY"};
    private String[] daysNL ={"MA","DI","WO","DO","VR"};
    private ArrayList<String> searchResult;
    private String subjectInfo; // this depends on whether it's a teacher or group, returns different things
    public RoosterAPI(){
        searchResult = new ArrayList<>();
    }

    public ArrayList<String> search(String query) throws Exception{ // searches
        Communication APIsearch = new Communication("http://api.roosters.saxion.nl/v2/search.json?q=");
        String servResponse =APIsearch.APIrequest(query).toString(); // parsing JSON
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

    public ArrayList<String> schedule(String query, String type, String day) throws Exception{
        boolean isDayRequested; // checks if user requested a day
        String servResponse; // response from the server
        JSONObject reply; // JSON parsing objects
        JSONArray weekSchedule;
        JSONArray daySchedule;
        if (type.equals("group")){ // checks type of request - for a group or a teacher
            Communication APIschedule= new Communication("http://api.roosters.saxion.nl/v2/schedule.json?group=");
            servResponse = APIschedule.APIrequest(query).toString();  // parsing JSON
            subjectInfo = "teachername";
        }else{
            Communication APIschedule= new Communication("http://api.roosters.saxion.nl/v2/schedule.json?teacher=");
            servResponse = APIschedule.APIrequest(query).toString();  // parsing JSON
            subjectInfo = "group_id";
        }
        try {
            reply = new JSONObject(servResponse);
            if ((!day.equals(""))) { // if there is a day requested
                isDayRequested = true;
            } else { // if there is not
                isDayRequested = false; // get some metadata about the week
                String subjectName = reply.getJSONObject("subject").getJSONObject(type).getString("name");
                String week = reply.getJSONObject("week").getString("qweek");
                searchResult.add("Week " + week + " for " + subjectName + " looks like this:\n");
            }
            weekSchedule = reply.getJSONArray("days");
            if (!isDayRequested) { // if a day was not requested
                for (int i = 0; i < weekSchedule.length(); i++) { // run through the week
                    searchResult.add(weekSchedule.getJSONObject(i).getJSONObject("date").getString("day") + ":\n");
                    daySchedule = weekSchedule.getJSONObject(i).getJSONArray("entries");
                    if ((daySchedule.length() > 0)) { // run through every day
                        addResults(daySchedule); // method makes code more compact
                    } else { // if nothing send a prompt
                        searchResult.add("No lectures!\n");
                    }
                }
            } else { // if it was
                for (int k = 0; k < daysNL.length; k++) { // run through the week
                    if (day.toUpperCase().equals(daysEN[k])) { // but now check for a specific day
                        searchResult.add(weekSchedule.getJSONObject(k).getJSONObject("date").getString("day") + ":\n");
                        daySchedule = weekSchedule.getJSONObject(k).getJSONArray("entries");
                        if ((daySchedule.length() > 0)) { // when the day was found get info for it
                            addResults(daySchedule);
                        } else {
                            searchResult.add("Nothing scheduled!\n");
                        }
                    }
                }
                if (searchResult.size() == 0){
                    searchResult.add("Did you get the day correctly? I can't find anything on it.");
                }
            }
        }catch(JSONException e){
            e.printStackTrace();
            searchResult.add("It appears you search for something that does not exist." +
                    "\n Do that in science, not here ;)");
        }
        return searchResult;
    }
    private void addResults(JSONArray daySchedule){ // handles data adding and exception handling
        for (int j=0;j<daySchedule.length();j++) {
            try {
                searchResult.add(daySchedule.getJSONObject(j).getString("start") + " - " +
                        daySchedule.getJSONObject(j).getString("end"));
            } catch (JSONException e) {
                searchResult.add("No time specified.");
            }
            try {
                searchResult.add(daySchedule.getJSONObject(j).getString("name_en"));
            } catch (JSONException e) {
                searchResult.add("No english name specified.");
            }
            try {
                searchResult.add(daySchedule.getJSONObject(j).getString(subjectInfo));
            } catch (JSONException e) {
                searchResult.add("No additional info specified.");
            }
            try {
                searchResult.add(daySchedule.getJSONObject(j).getString("room") + "\n");
            } catch (JSONException e) {
                searchResult.add("No room specified.");
            }
        }
    }
}
