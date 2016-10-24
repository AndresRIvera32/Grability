package com.example.jriverac.grability;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by jriverac on 23/10/2016.
 */

public class HttpAsyncTask extends AsyncTask<Void, Void, JSONArray> {

    private String url;

    public HttpAsyncTask(String url) {
        this.url=url;
    }


    public String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // crear el cliente http
            HttpClient httpclient = new DefaultHttpClient();

            // hacer el request para la url dada
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // recibir la respuesta como entrada de datos
            inputStream = httpResponse.getEntity().getContent();

            // convertir entrada de datos a cadena de caracteres
            if (inputStream != null) {
                result = convertInputStreamToString(inputStream);
            } else {
                result = "no funciono!";
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    public String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    protected JSONArray doInBackground(Void... params) {
        this.url= GET(this.url);
        JSONObject jsonObj = null;
        JSONArray arrayJson = null;
        try {
            jsonObj = new JSONObject(this.url);
            jsonObj = new JSONObject(jsonObj.getString("feed"));
            arrayJson = jsonObj.getJSONArray("entry");
            return arrayJson;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayJson;
    }
    // onPostExecute displays the results of the AsyncTask.

    protected void onPostExecute(JSONArray result) {
        super.onPostExecute(result);
    }
}
