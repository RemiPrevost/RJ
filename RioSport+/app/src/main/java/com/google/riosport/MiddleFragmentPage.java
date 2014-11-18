package com.google.riosport;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MiddleFragmentPage extends Fragment {
    private ImageView testimage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View middleview = inflater.inflate(R.layout.middle_page_layout, container, false);
        //testimage = (ImageView) middleview.findViewById(R.id.testimage);
        /*try{
            File f= new File(Main.picture, "profile.png");
            Bitmap image = BitmapFactory.decodeStream(new FileInputStream(f));
            testimage.setImageBitmap(image);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }*/
        return middleview;


    }



}