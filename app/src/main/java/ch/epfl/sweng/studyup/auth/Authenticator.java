package ch.epfl.sweng.studyup.auth;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import ch.epfl.sweng.studyup.player.Player;

/**
 * Authenticator
 *
 * Link between the application and the authentification server.
 */
public class Authenticator {
    /**
     * Extract a String JSON object from an URL.
     *
     * @param requestURL The given URL.
     * @return The JSON object in String format.
     */
    public static String getResponse(String requestURL) {

        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(requestURL).openConnection();
            InputStream stream = connection.getInputStream();
            String response = new Scanner(stream).useDelimiter("\\A").next();

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Format the informations in a String JSON object into a readable form.
     *
     * @param response The JSON object in String format.
     * @return The formatted informations.
     */
    public static String getGreetingFromResponse(String response) {

        System.out.println(response);
        JSONProfileContainer profileContainer = new Gson().fromJson(response,
                JSONProfileContainer.class);

        if (profileContainer.error == null && profileContainer.sciperNo != null) {

            String greeting = "";

            Player currPlayer = Player.get();
            currPlayer.setSciper(Integer.parseInt(profileContainer.sciperNo));

            if (profileContainer.firstName != null) {
                greeting += "Welcome, " + profileContainer.firstName + ".\n";
                currPlayer.setFirstName(profileContainer.firstName);
            }
            if (profileContainer.lastname != null) {
                currPlayer.setLastName(profileContainer.lastname);
            }
            greeting += "Your Sciper number is " + profileContainer.sciperNo + ".";

            return greeting;
        }

        return null;
    }

    public static String getGreeting(String token) {

        String requestURL = "https://tequila.epfl.ch/cgi-bin/OAuth2IdP/userinfo" + "?access_token="
                + token;
        String response = getResponse(requestURL);

        return getGreetingFromResponse(response);
    }

    /**
     * Extract the token from a String JSON object.
     *
     * @param response JSON object.
     * @return Token.
     */
    public static String getTokenFromResponse(String response) {
        JSONTokenContainer tokenContainer = new Gson().fromJson(response, JSONTokenContainer.class);

        if (tokenContainer.error == null && tokenContainer.token != null) {
            String token = tokenContainer.token;
            try {
                token = URLEncoder.encode(token, "UTF-8");
                return token;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static String getToken(String code) {

        String requestURL = "https://studyup-authenticate.herokuapp.com/getToken" + "?code=" + code;
        String response = getResponse(requestURL);

        return getTokenFromResponse(response);
    }

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

        @SerializedName("Name")
        public String lastname;

        @SerializedName("Sciper")
        public String sciperNo;
    }
}
