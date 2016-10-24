package com.example.jriverac.grability;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by jriverac on 23/10/2016.da
 */
public class DetalleApp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalleapp);

        Button btn2 = (Button) findViewById(R.id.botonact2);
        String nombre=getIntent().getStringExtra("nombre");
        String detalle=getIntent().getStringExtra("detalle");
        String imagen=getIntent().getStringExtra("imagen");
        String precio=getIntent().getStringExtra("precio");
        precio=precio+"  USD";
        List<Bitmap> Bm=null;
        ImageView imgv= (ImageView) findViewById(R.id.imgV);
        TextView imgv2= (TextView) findViewById(R.id.tx10);
        imgv2.setText(precio);
        try {
            Bm = new ImageLoadTask(null,imagen).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if(Bm!=null){
            imgv.setImageBitmap(Bm.get(0).createScaledBitmap(Bm.get(0),180,180,false));
        }
        TextView tx= (TextView) findViewById(R.id.textvw);
        tx.setMovementMethod(new ScrollingMovementMethod());
        TextView tx2= (TextView) findViewById(R.id.tx6);
        tx2.setMovementMethod(new ScrollingMovementMethod());

        tx.setText(nombre);
        tx2.setText(detalle);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent (v.getContext(), MainActivity.class);
                startActivityForResult(intent2, 0);
            }
        });
    }
}
