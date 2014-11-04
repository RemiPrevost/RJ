package com.google.riosport;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by pierre-alexandremaury on 20/10/2014.
 */
public class customTextView extends TextView {

        public customTextView(Context context) {
            super(context);
            setFont();
        }
        public customTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            setFont();
        }
        public customTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            setFont();
        }

        private void setFont() {
            Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                    "JeNeTOublieraiJamais.ttf");
            setTypeface(font, Typeface.NORMAL);
        }
    }
