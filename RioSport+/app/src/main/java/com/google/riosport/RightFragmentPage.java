package com.google.riosport;



import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;



public class RightFragmentPage extends Fragment {

    private GoogleMap map;
    private MapView mapView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rightview = inflater.inflate(R.layout.right_page_layout, container, false);
        //code
        mapView = (MapView) rightview.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        map=mapView.getMap();
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        MapsInitializer.initialize(getActivity());
        map.setMyLocationEnabled(true);
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