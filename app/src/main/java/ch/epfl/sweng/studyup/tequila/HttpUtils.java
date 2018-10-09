package ch.epfl.sweng.studyup.tequila;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

/**
 * Utilities for HTTP requests.
 *
 * @author Solal Pirelli
 */
public final class HttpUtils {
    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 is not supported... all hope is lost.");
        }
    }

    public static <T> T fetch(String url, Class<T> classOfT) throws IOException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            InputStream stream = connection.getInputStream();
            String json = new Scanner(stream).useDelimiter("\\A").next();
            return new Gson().fromJson(json, classOfT);
        } catch (MalformedURLException e) {
            throw new AssertionError("The URL is malformed!?");
        }
    }
}
