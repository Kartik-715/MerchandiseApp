package com.example.merchandiseapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageAdapter extends PagerAdapter
{
    private Context mContext;
    private ArrayList<String> image;
    String flag;
    //private int[] mImageIds = new int[] {R.drawable.fui_ic_facebook_white_22dp, R.drawable.common_google_signin_btn_icon_dark};

    ImageAdapter(Context context, ArrayList<String> image, String flag)
    {
        mContext = context;
        this.image = image;
        this.flag = flag;
    }

    @Override
    public int getCount()
    {
        return image.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o)
    {
        return view == o ;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position)
    {
        ImageView imageView = new ImageView(mContext);

        if(flag.equals("1"))
        {
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            PhotoViewAttacher pAttacher;
            pAttacher = new PhotoViewAttacher(imageView);
            pAttacher.update();
            Picasso.get().load(image.get(position)).into(imageView);

            container.addView(imageView, 0);
            return imageView;
        }

        else if(flag.equals("0"))
        {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            /*PhotoViewAttacher pAttacher;
            pAttacher = new PhotoViewAttacher(imageView);
            pAttacher.update();*/
            Picasso.get().load(image.get(position)).into(imageView);

            container.addView(imageView, 0);
            imageView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    System.out.println("Chiragggg");
                    Intent intent = new Intent(mContext, FullPageImageActivity.class);
                    intent.putExtra("image", image);
                    mContext.startActivity(intent);
                }
            });
            return imageView;
        }

        return imageView;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object)
    {
        container.removeView((ImageView) object);
    }
}
