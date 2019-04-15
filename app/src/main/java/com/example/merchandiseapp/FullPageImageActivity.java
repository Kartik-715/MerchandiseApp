package com.example.merchandiseapp;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FullPageImageActivity extends AppCompatActivity
{
    private String image;
    private ImageView displayImage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_page_image);
        image = getIntent().getStringExtra("image");
        displayImage = findViewById(R.id.displayImage);

        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(displayImage);
        pAttacher.update();

        Picasso.get().load(image).into(displayImage);
    }
}
