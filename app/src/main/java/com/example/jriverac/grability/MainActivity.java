package com.example.jriverac.grability;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText etResponse;
    TextView tvIsConnected;
    public static ArrayList<Aplicacion> app=null;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app= new ArrayList<>();
        // get reference to the views
        //etResponse = (EditText) findViewById(R.id.etResponse);
        // check if you are connected or not
        if(isConnected()){
            Toast.makeText(getApplicationContext(), "conectado!", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getBaseContext(), "desconectado!", Toast.LENGTH_LONG).show();
        }
        JSONArray arrayJson=null;
        GridLayout gridLayout= (GridLayout)findViewById(R.id.tblay1);
        List<Bitmap> Bm=null;
        List<String> lista=new ArrayList<String>();
        // llamamos al AsynTask para que nos traiga el JSON
            try {
                arrayJson = new HttpAsyncTask("https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json").execute().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            // llenamos la lista de aplicacion con lo que trae el JSON
            llenarArrayApps(arrayJson, arrayJson.length());
            for(Aplicacion a:app) {
                lista.add(a.getImagen());
            }
        try {
            //traemos el bitmap de cada imagen
            if(arrayJson!=null) {
                Bm = new ImageLoadTask(app,null).execute().get();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        gridLayout.removeAllViews();
        if(Bm!=null) {
            int total = 20;
            int column = 4;
            int row = total / column;
            gridLayout.setColumnCount(column);
            gridLayout.setRowCount(row + 1);
            for (int i = 0, c = 0, r = 0; i < total; i++, c++) {
                if (c == column) {
                    c = 0;
                    r++;
                }
                ImageButton oImageView = new ImageButton(getApplicationContext());
                //BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), Bm.get(i).createScaledBitmap(Bm.get(i),180,180,false));
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), Bm.get(i).createScaledBitmap(Bm.get(i),180,180,false));
                oImageView.setBackground(bitmapDrawable);
                GridLayout.LayoutParams param = new GridLayout.LayoutParams();
                param.height = GridLayout.LayoutParams.WRAP_CONTENT;
                param.width = GridLayout.LayoutParams.WRAP_CONTENT;
                param.bottomMargin = 50;
                param.leftMargin = 20;
                param.rightMargin = 20;
                param.topMargin = 50;
                param.setGravity(Gravity.CENTER);
                param.columnSpec = GridLayout.spec(c);
                param.rowSpec = GridLayout.spec(r);
                oImageView.setLayoutParams(param);
                final String nombre=app.get(i).getNombre();
                final String detalle=app.get(i).getDetalle();
                final String imagen=app.get(i).getImagen();
                final String precio=app.get(i).getPrecio();
                oImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), DetalleApp.class);
                        intent.putExtra("nombre",nombre);
                        intent.putExtra("detalle",detalle);
                        intent.putExtra("imagen",imagen);
                        intent.putExtra("precio",precio);
                        startActivityForResult(intent, 0);
                    }
                });
                gridLayout.addView(oImageView);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(item.getItemId()==R.id.action_search){
                    Intent intent = new Intent(getApplicationContext(), DetalleApp.class);
                    startActivityForResult(intent, 0);
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void llenarArrayApps(JSONArray js,int tam){

        for(int i=0;i<tam;i++){
            try {
                JSONArray JS = js.getJSONObject(i).getJSONArray("im:image");
                String url =JS.getJSONObject(2).getString("label");
                String categoria=js.getJSONObject(i).getJSONObject("category").getJSONObject("attributes").getString("term");
                String precio=js.getJSONObject(i).getJSONObject("im:price").getJSONObject("attributes").getString("amount");
                String name=js.getJSONObject(i).getJSONObject("im:name").getString("label");
                String summary=js.getJSONObject(i).getJSONObject("summary").getString("label");
                app.add(new Aplicacion(name,url,summary,categoria, precio));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}





