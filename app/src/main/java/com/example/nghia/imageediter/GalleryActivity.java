package com.example.nghia.imageediter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class GalleryActivity extends AppCompatActivity{
    ImageView img;
    Toolbar toolbar;
    Uri uri;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar_gallery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Image from");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        img = (ImageView) findViewById(R.id.imgDetail);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            String uri_Str= b.getString("Image");
            uri = Uri.parse(uri_Str);
            Log.d("MY"," Da nhannn");
            img.setImageURI(uri);
        }
        else {
            Log.d("MY", "Da nhan.........");
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tick, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.tick) {
            Intent t  = new Intent(GalleryActivity.this, Edittor.class);
            t.putExtra("pic",uri.toString());
            startActivity(t);
        }
        return super.onOptionsItemSelected(item);
    }
}
