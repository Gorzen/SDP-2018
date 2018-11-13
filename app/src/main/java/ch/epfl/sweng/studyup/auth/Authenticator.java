package ch.epfl.sweng.studyup.auth;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import static ch.epfl.sweng.studyup.utils.Constants.*;
import static ch.epfl.sweng.studyup.utils.DataContainers.*;

/**
 * Authenticator
 *
 * Link between the application and the authentification server.
 */
public class Authenticator {

    public static String getToken(String code) throws Exception {

        String requestURL = AUTH_SERVER_TOKEN_URL + code;
        String response = getResponse(requestURL);

        return getTokenFromResponse(response);
    }

    public static String getTokenFromResponse(String response) throws Exception {

        TokenContainer tokenContainer = new Gson().fromJson(response, TokenContainer.class);

        String responseError = tokenContainer.error;
        String token = tokenContainer.token;

        if (responseError != null) {
            throw new Exception("Error when getting token: " + responseError);
        }
        if (token == null) {
            throw new Exception("Unable to retrieve token.");
        }

        return URLEncoder.encode(token, "UTF-8");
    }

    public static PlayerDataContainer getPlayerData(String token) throws Exception {

        String requestURL = TEQUILA_AUTH_URL + token;
        String response = getResponse(requestURL);

        PlayerDataContainer playerData = new Gson().fromJson(response, PlayerDataContainer.class);
        String error = playerData.error;

        if (error != null) {
            throw new Exception("Error when getting player data: " + error);
        }
        if (playerData.sciperNum == null) {
            throw new Exception("Unable to retrieve player data.");
        }
    }

    public static String getResponse(String requestURL) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) new URL(requestURL).openConnection();
        InputStream stream = connection.getInputStream();
        return new Scanner(stream).useDelimiter("\\A").next();
    }
}
