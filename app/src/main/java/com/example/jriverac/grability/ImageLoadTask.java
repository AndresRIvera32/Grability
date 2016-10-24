package com.example.jriverac.grability;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jriverac on 19/10/2016.
 */

public class ImageLoadTask extends AsyncTask<Void, Void, List<Bitmap>> {

    private List<String> url;
    private String cadena;
    private ImageButton imageButton;
    private List<Aplicacion> app;
    private Context context;

    public ImageLoadTask(List<Aplicacion> app,String a) {
        this.url = url;
        this.imageButton = imageButton;
        this.app=app;
        this.cadena=a;
        this.context=context;
    }

    @Override
    protected List<Bitmap> doInBackground(Void... params) {

    List<Bitmap> Bm=new ArrayList<Bitmap>();
        try {
                if(this.cadena==null) {
                    for (Aplicacion a : app) {
                        URL urlConnection = new URL(a.getImagen());
                        HttpURLConnection connection = (HttpURLConnection) urlConnection
                                .openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);
                        Bm.add(myBitmap);
                    }
                    return Bm;
                }
                if(this.cadena!=null){
                    URL urlConnection = new URL(this.cadena);
                    HttpURLConnection connection = (HttpURLConnection) urlConnection
                            .openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    Bm.add(myBitmap);
                    return Bm;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Bitmap> result) {
        super.onPostExecute(result);
    }

}
