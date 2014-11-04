package com.google.riosport;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class LeftFragmentPage extends Fragment {
    private ImageView profilepicture;
    private TextView profile;
    private Button friend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View leftleview = inflater.inflate(R.layout.left_page_layout, container, false);

        profile = (TextView) leftleview.findViewById(R.id.name);
        friend = (Button) leftleview.findViewById(R.id.friendbutton);
        profilepicture = (ImageView) leftleview.findViewById(R.id.profilpicture);
        profile.setText(Main.txtName);
        profile.setTextSize(15);
        friend.setTextSize(15);

        try{
            File f= new File(Main.picture, "profile.png");
            Bitmap image = BitmapFactory.decodeStream(new FileInputStream(f));
            profilepicture.setImageBitmap(getCircleBitmap(image));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        friend.setText(R.string.addfriend);

        //code
        return leftleview;
    }

    private Bitmap getCircleBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}