/**
 * Created by Shashank Degloorkar on 20-Jan-2019
 */
package com.sd.example.sdcircleimageview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sdcircleimageview.SDCircleImageView;

public class MainActivity extends AppCompatActivity {

    SDCircleImageView sdCircleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Resources resources = getResources();
        Bitmap icon_1 = BitmapFactory.decodeResource(resources, R.drawable.image_dog_6);
        sdCircleView = findViewById(R.id.sdCircleView);
        sdCircleView.setInnerBitmapImage(icon_1);
    }
}
