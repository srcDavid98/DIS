import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A utility class for the methods used to make HTTP requests.
 */
public class NetworkUtil {

    /**
     * Makes a HTTP request without a body (GET).
     * @param url The URL of the resource to make the request to.
     * @param requestType The type of request. Should be GET.
     * @return The result of the request.
     * @throws IOException From the underlying calls to network related methods.
     */
    public String httpRequest(String url, String requestType) throws IOException {
        return httpRequest(url, requestType, null);
    }

    /**
     * Makes a HTTP request with a body (POST/PUT).
     * @param url The URL of the resource to make the request to.
     * @param requestType The type of request. Should be POST/PUT.
     * @param body The body to be posted/putted.
     * @return The result of the request.
     * @throws IOException From the underlying calls to network related methods.
     */
    public String httpRequest(String url, String requestType, String body) throws IOException {
        String responseString = null;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        //HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

        con.setRequestMethod(requestType);
        con.setDoOutput(true);

        if (body != null && !body.isEmpty()) {
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = body.getBytes();
                os.write(input, 0, input.length);
            }
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        in.close();
        responseString = response.toString();

        return responseString;
    }
}
