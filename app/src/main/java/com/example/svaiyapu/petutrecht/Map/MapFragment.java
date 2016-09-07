package com.example.svaiyapu.petutrecht.Map;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.svaiyapu.petutrecht.Detail.DetailActivity;
import com.example.svaiyapu.petutrecht.R;
import com.example.svaiyapu.petutrecht.Util.DynamicHeightImageView;
import com.example.svaiyapu.petutrecht.Util.IntentUtil;
import com.example.svaiyapu.petutrecht.Util.PetUtil;
import com.example.svaiyapu.petutrecht.data.Model.Location;
import com.example.svaiyapu.petutrecht.data.Model.Pet;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by svaiyapu on 8/18/16.
 */
public class MapFragment extends Fragment implements MapContract.View, OnMapReadyCallback {

    private GoogleMap mMap;
    private MapContract.Presenter mPresenter;
    private MapView mMapView;

    private static final String LOG_TAG = "MapFragment";
    // zoom level of 12 shows the whole utrecht city
    private static final int ZOOM_LEVEL = 12;

    public MapFragment() {
        // Requires empty public constructor
    }

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        mPresenter = new MapPresenter(this);

        // Obtain the MapView and get notified when the map is ready to be used.
        mMapView = (MapView) v.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        return v;
    }

    @Override
    public void setPresenter(MapContract.Presenter presenter) {
        mPresenter = presenter;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Ask the presenter to fetch the pet list from presenter
        mPresenter.loadPets();
    }

    /**
     * Called by presenter when data is ready
     * Add markers to the map and do other map setup.
     * @param pets List of pets
     */
    @Override
    public void showMapMarkers(List<Pet> pets) {
        for(Pet pet: pets){
            Location location = pet.getLocation();
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Marker petMarker = mMap.addMarker(new MarkerOptions().position(latLng)
                    .title(pet.getName())
                    .snippet(pet.getBreed())
                    .icon(BitmapDescriptorFactory.fromResource(
                            PetUtil.getIconResource(pet.getType()) //get cat/Dog icon
                    ))
            );
            petMarker.setTag(pet);
        }
        //Build camera position
        Location camZoomLocation = pets.get(0).getLocation();
        LatLng latLng = new LatLng(camZoomLocation.getLatitude(), camZoomLocation.getLongitude());
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)
                .zoom(ZOOM_LEVEL).build();
        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {
                // Getting view from the layout file info_window_layout
                final View v = LayoutInflater.from(getActivity()).inflate(R.layout.info_window_item, null);

                final Pet pet = (Pet) marker.getTag();

                final TextView titleTextView = (TextView) v.findViewById(R.id.infowindow_titleTextView);
                final TextView breedTextView = (TextView) v.findViewById(R.id.infowindow_typeTextView);
                final ImageView petImageView = (ImageView) v.findViewById(R.id.infowindow_imageView);

                titleTextView.setText(pet.getName());
                breedTextView.setText(pet.getBreed());

                Picasso.with(getActivity())
                        .load(pet.getImg_primary())
                        .placeholder(R.drawable.placeholder)
                        .into(petImageView, new InfoWindowRefresher(marker));

                // Returning the view containing InfoWindow contents
                return v;
            }
        });
        // Set a listener for info window events.
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(IntentUtil.GRID_TO_DETAIL_PET_NAME, marker.getTitle());
                intent.putExtra(IntentUtil.GRID_TO_DETAIL_PET_TYPE, IntentUtil.MAP_TO_DETAIL_PET_TYPE);
                startActivity(intent);
            }
        });
    }

    /**
     *  all the life cycle methods from the Fragment containing a map view
     *  must be forwarded to the corresponding ones in the mapview.
      */
    @Override
    public void onResume() {
        super.onResume();
        if(mMapView != null) {
            mMapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mMapView != null) {
            mMapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMapView != null) {
            mMapView.onDestroy();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mMapView != null) {
            mMapView.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if(mMapView != null) {
            mMapView.onLowMemory();
        }
    }

    private class InfoWindowRefresher implements  com.squareup.picasso.Callback {
        private Marker marker;

        private InfoWindowRefresher(Marker markerToRefresh) {
            this.marker = markerToRefresh;
        }

        @Override
        public void onSuccess() {
            if (marker != null && marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
                marker.showInfoWindow();
            }
        }

        @Override
        public void onError() {}
    }

}
