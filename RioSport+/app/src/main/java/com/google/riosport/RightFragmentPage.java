package com.google.riosport;



import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;


public class RightFragmentPage extends Fragment {

    private GoogleMap map;
    private MapView mapView;
    private Button event;
    private Location user_location;
    private String provider;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rightview = inflater.inflate(R.layout.right_page_layout, container, false);
        event = (Button) rightview.findViewById(R.id.create_event);
        mapView = (MapView) rightview.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        map=mapView.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        MapsInitializer.initialize(getActivity());
        map.setMyLocationEnabled(true);

        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria=new Criteria();
        provider=lm.getBestProvider(criteria, false);
        user_location=lm.getLastKnownLocation(provider);
        if(user_location!=null){
            double longitude = user_location.getLongitude();
            double latitude = user_location.getLatitude();
            LatLng point = new LatLng(latitude, longitude);
            CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(point, 13);
            map.moveCamera(cameraPosition);
            map.animateCamera(cameraPosition);
        }

        event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateEvent.class);
                startActivityForResult(intent, 10);
            }
        });
        return rightview;
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}