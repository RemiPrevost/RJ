package com.google.riosport;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;
import java.util.Calendar;


public class Main extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private TextView textdate = null;
    private int year;
    private int month;
    private int day;
    private int day_week;
    private String monthString = null;
    private String dayString = null;

    private static final int DIALOG_GET_GOOGLE_PLAY_SERVICES = 1;
    private static final String TAG = "Main";
    private static final int REQUEST_CODE_SIGN_IN = 1;
    private static final int REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES = 2;

    private TextView mSignInStatus, txtName, txtEmail;
    private GoogleApiClient mGoogleApiClient;
    private SignInButton mSignInButton;
    private View mSignOutButton;
    private ConnectionResult mConnectionResult;
    private View mRevokeAccessButton;
    private LinearLayout llProfileLayout;
    private ImageView imgProfilePic, christ;
    private com.google.riosport.customTextView titlefrag1;
    private com.google.riosport.customTextView titlefrag2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setCurrentDate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Plus.API, Plus.PlusOptions.builder()
                        .addActivityTypes("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity").build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mSignInStatus = (TextView) findViewById(R.id.sign_in_status);
        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(this);
        mSignOutButton = findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(this);
        mRevokeAccessButton = findViewById(R.id.revoke_access_button);
        mRevokeAccessButton.setOnClickListener(this);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        christ = (ImageView) findViewById(R.id.christ);
        titlefrag1 = (com.google.riosport.customTextView) findViewById(R.id.titleFrag1);
        titlefrag2 = (com.google.riosport.customTextView) findViewById(R.id.titleFrag2);
        customActionBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.sign_in_button:
                int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
                if (available != ConnectionResult.SUCCESS) {
                    showDialog(DIALOG_GET_GOOGLE_PLAY_SERVICES);
                    return;
                }

                try {
                    mSignInStatus.setText(getString(R.string.signing_in_status));
                    mConnectionResult.startResolutionForResult(this, REQUEST_CODE_SIGN_IN);
                } catch (IntentSender.SendIntentException e) {
                    // Fetch a new result to start.
                    mGoogleApiClient.connect();
                }
                break;
            case R.id.revoke_access_button:
                if (mGoogleApiClient.isConnected()) {
                    mSignInStatus.setVisibility(View.VISIBLE);
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient).setResultCallback(
                            new ResultCallback<Status>() {
                                @Override
                                public void onResult(Status status) {
                                    if (status.isSuccess()) {
                                        mSignInStatus.setText(R.string.revoke_access_status);
                                    } else {
                                        mSignInStatus.setText(R.string.revoke_access_error_status);
                                    }
                                    mGoogleApiClient.disconnect();
                                    mGoogleApiClient.connect();
                                }
                            }
                    );
                    updateButtons(false /* isSignedIn */);

                }
                break;
            case R.id.sign_out_button:
                if (mGoogleApiClient.isConnected()) {
                    mSignInStatus.setVisibility(View.VISIBLE);
                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                }
                break;

        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id != DIALOG_GET_GOOGLE_PLAY_SERVICES) {
            return super.onCreateDialog(id);
        }

        int available = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS) {
            return null;
        }
        if (GooglePlayServicesUtil.isUserRecoverableError(available)) {
            return GooglePlayServicesUtil.getErrorDialog(
                    available, this, REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES);
        }
        return new AlertDialog.Builder(this)
                .setMessage(R.string.plus_generic_error)
                .setCancelable(true)
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SIGN_IN
                || requestCode == REQUEST_CODE_GET_GOOGLE_PLAY_SERVICES) {
            if (resultCode == RESULT_CANCELED) {
                mSignInStatus.setText(getString(R.string.signed_out_status));
            } else if (resultCode == RESULT_OK && !mGoogleApiClient.isConnected()
                    && !mGoogleApiClient.isConnecting()) {
                // This time, connect should succeed.
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mSignInStatus.setVisibility(View.INVISIBLE);
        getProfileInformation();
        updateButtons(true /* isSignedIn */);
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mSignInStatus.setText(R.string.loading_status);
        mGoogleApiClient.connect();
        updateButtons(false /* isSignedIn */);
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        mConnectionResult = result;
        updateButtons(false /* isSignedIn */);
    }

    private void updateButtons(boolean isSignedIn) {
        if (isSignedIn) {
            mSignInButton.setVisibility(View.INVISIBLE);
            mSignOutButton.setEnabled(true);
            mRevokeAccessButton.setEnabled(true);
            llProfileLayout.setVisibility(View.VISIBLE);
            christ.setVisibility(View.INVISIBLE);
            titlefrag1.setVisibility(View.INVISIBLE);
            titlefrag2.setVisibility(View.INVISIBLE);
        } else {
            if (mConnectionResult == null) {
                // Disable the sign-in button until onConnectionFailed is called with result.
                mSignInButton.setVisibility(View.INVISIBLE);
                mSignInStatus.setText(getString(R.string.loading_status));
            } else {
                // Enable the sign-in button since a connection result is available.
                mSignInButton.setVisibility(View.VISIBLE);
                mSignInStatus.setText(getString(R.string.signed_out_status));
                llProfileLayout.setVisibility(View.INVISIBLE);
                christ.setVisibility(View.VISIBLE);
                titlefrag1.setVisibility(View.VISIBLE);
                titlefrag2.setVisibility(View.VISIBLE);
            }

            mSignOutButton.setEnabled(false);
            mRevokeAccessButton.setEnabled(false);
        }
    }

    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String id = currentPerson.getId();
                String personName = currentPerson.getDisplayName();
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl + ", id: " + id);

                txtName.setText("Sign in as : " + personName);
                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + 400;

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
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




