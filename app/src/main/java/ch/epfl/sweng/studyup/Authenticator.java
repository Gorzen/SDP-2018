package ch.epfl.sweng.studyup;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Authenticator {

    private static final class JSONTokenContainer {
        @SerializedName("error")
        public String error;

        @SerializedName("access_token")
        public String token;
    }

    private static final class JSONProfileContainer {
        @SerializedName("error")
        public String error;

        @SerializedName("Firstname")
        public String firstName;

        @SerializedName("Sciper")
        public String sciperNo;
    }

    public static String getResponse(String requestURL) {

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(requestURL).openConnection();
            InputStream stream = connection.getInputStream();
            String response = new Scanner(stream).useDelimiter("\\A").next();

            return response;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getGreetingFromResponse(String response) {

        System.out.println(response);
        JSONProfileContainer profileContainer = new Gson().fromJson(response, JSONProfileContainer.class);

        if (profileContainer.error == null && profileContainer.sciperNo != null) {

            String greeting = "";

            Player currPlayer = Player.get();
            currPlayer.setSciper(Integer.parseInt(profileContainer.sciperNo));

            if (profileContainer.firstName != null) {
                greeting += "Welcome, " + profileContainer.firstName + ".\n";
                currPlayer.setFirstName(profileContainer.firstName);
            }
            greeting += "Your Sciper number is " + profileContainer.sciperNo + ".";

            return greeting;
        }

        return null;
    }

    public static String getGreeting(String token) {

        String requestURL = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/userinfo" + "?access_token=" + token;
        String response = getResponse(requestURL);

        return getGreetingFromResponse(response);
    }

    public static String getTokenFromResponse(String response) {
        JSONTokenContainer tokenContainer = new Gson().fromJson(response, JSONTokenContainer.class);

        if (tokenContainer.error == null && tokenContainer.token != null) {
            String token = tokenContainer.token;
            try {
                token = URLEncoder.encode(token, "UTF-8");
                return token;
            }
            catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        return null;
    }

    public static String getToken(String code) {

        String requestURL = "https://studyup-authenticate.herokuapp.com/getToken" + "?code=" + code;
        String response = getResponse(requestURL);

        return getTokenFromResponse(response);
    }
}
