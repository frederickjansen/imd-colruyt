package be.alfredo.colruyt;

import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Helper class to communicate with server.
 */
public class ServerUtilities
{
    private final static String TAG = "ServerUtilities";

    /**
     * POST data to a server and get response back.
     *
     * @param endpoint URL to POST to.
     * @param params Request parameters
     * @return
     * @throws IOException Response code not 200
     */
    public static String postAndGet(String endpoint, Map<String, String> params) throws IOException
    {
        URL url = null;
        try
        {
            url = new URL(endpoint);
        }
        catch (MalformedURLException e)
        {
            throw new IllegalArgumentException("Invalid url " + endpoint);
        }

        String body = paramsToString(params);
        HttpURLConnection conn = null;
        InputStream in = null;
        try
        {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setFixedLengthStreamingMode(body.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            OutputStream out = conn.getOutputStream();
            out.write(body.getBytes());
            out.close();

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK)
            {
                in = conn.getInputStream();
                String response = inputStreamToString(in);
                return response;
            }
            else
            {
                throw new IOException("Response code " + responseCode + " received.");
            }
        }
        catch (IOException e)
        {
            Log.e(TAG, "Can't open connection");
        }
        finally
        {
            if (in != null)
            {
                in.close();
            }
            if (conn != null)
            {
                conn.disconnect();
            }
        }

        return null;
    }

    /**
     * Create a POST parameter string.
     *
     * @param params Map of POST keys and values.
     * @return POST parameters as string.
     */
    private static String paramsToString(Map<String, String> params)
    {
        if(params.size() == 0)
        {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet())
        {
            builder.append(builder.length() == 0 ? "" : "&");
            builder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return builder.toString();
    }

    /**
     * Convert input stream to string.
     *
     * @param in Input stream
     * @see <a href="http://stackoverflow.com/questions/2492076/android-reading-from-an-input-stream-efficiently">http://stackoverflow.com/questions/2492076/android-reading-from-an-input-stream-efficiently</a>
     * @return Input stream as string.
     */
    private static String inputStreamToString(InputStream in)
    {
        BufferedReader r = new BufferedReader(new InputStreamReader(in));
        StringBuilder total = new StringBuilder();
        String line;

        try
        {
            while ((line = r.readLine()) != null)
            {
                total.append(line);
            }
        }
        catch (IOException e){}

        return total.toString();
    }
}
