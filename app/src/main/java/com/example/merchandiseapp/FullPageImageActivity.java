package com.example.merchandiseapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FullPageImageActivity extends AppCompatActivity
{
    private ArrayList<String> image;
    private ImageView displayImage;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_page_image);
        image = getIntent().getStringArrayListExtra("image");
        displayImage = findViewById(R.id.displayImage);

        ViewPager viewPager = findViewById(R.id.ViewPager_Image);
        ImageAdapter adapter = new ImageAdapter(this, image);
        viewPager.setAdapter(adapter);
    }

}
