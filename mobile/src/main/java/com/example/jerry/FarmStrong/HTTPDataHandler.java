package com.example.jerry.FarmStrong;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HTTPDataHandler {
    static String stream = null;

    public HTTPDataHandler(){

    }

    public String GetHTTPData(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

            if(urlConnection.getResponseCode()==200)
            {
                urlConnection.setRequestProperty("Prediction=Key", "16ab01ac6f7f43208676bb27813897fc");
                urlConnection.setRequestProperty("Content-Type", "application/octet-stream");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while((line = r.readLine()) != null)
                {
                    sb.append(line);
                }
                stream = sb.toString();
                urlConnection.disconnect();
            }
            else
            {

            }
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return stream;
    }
}