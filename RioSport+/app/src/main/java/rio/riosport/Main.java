package rio.riosport;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.opengl.Matrix;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;


public class Main extends Activity {

    private TextView textdate = null;
    private int year;
    private int month;
    private int day;
    private int day_week;
    private String monthString = null;
    private String dayString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setCurrentDate();
        customActionBar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void customActionBar(){
        ActionBar mActionBar = getActionBar();
        mActionBar.setDisplayShowHomeEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.action_bar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

    }
    public void setCurrentDate() {
        //textdate = (TextView)findViewById(R.id.day);
        final Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        month++;
        switch (month) {
            case 1:  monthString = getString(R.string.jan);
                break;
            case 2:  monthString = getString(R.string.feb);
                break;
            case 3:  monthString = getString(R.string.mar);
                break;
            case 4:  monthString = getString(R.string.apr);
                break;
            case 5:  monthString = getString(R.string.may);
                break;
            case 6:  monthString = getString(R.string.jun);
                break;
            case 7:  monthString = getString(R.string.jul);
                break;
            case 8:  monthString = getString(R.string.aug);
                break;
            case 9:  monthString = getString(R.string.sep);
                break;
            case 10: monthString = getString(R.string.oct);
                break;
            case 11: monthString = getString(R.string.nov);
                break;
            case 12: monthString = getString(R.string.dec);
                break;}

        day = calendar.get(Calendar.DAY_OF_MONTH);
        day_week = calendar.get(Calendar.DAY_OF_WEEK);
        switch (day_week) {
            case(Calendar.MONDAY) : dayString = getString(R.string.mon);
                break;
            case(Calendar.TUESDAY) : dayString = getString(R.string.tue);
                break;
            case(Calendar.WEDNESDAY) : dayString = getString(R.string.wed);
                break;
            case(Calendar.THURSDAY) : dayString = getString(R.string.thu);
                break;
            case(Calendar.FRIDAY) : dayString = getString(R.string.fri);
                break;
            case(Calendar.SATURDAY) : dayString = getString(R.string.sat);
                break;
            case(Calendar.SUNDAY) : dayString = getString(R.string.sun);
                break;
        }

        // set current date into textview
        textdate.setText(new StringBuilder()
                // Month is 0 based, so you have to add 1
                .append(dayString).append(" ")
                .append(day).append(" ")
                .append(monthString).append(" ")
                .append(year).append(" "));
        // set current date into Date Picker
    }
}


