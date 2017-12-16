package com.ismek.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ismek.entity.ActiveLocationInfo;
import com.ismek.entity.BaseReturn;
import com.ismek.heytaksi.R;
import com.ismek.ws.ApiClient;
import com.ismek.ws.HeyTaksiRest;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapFragment extends Fragment implements OnMapReadyCallback{

    private GoogleMap mMap;

    private Double currentLat = 0d;
    private Double currentLng = 0d;

    Timer timer;
    TimerTask timerTask;

    final Handler handler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentLat = this.getArguments().getDouble("lat");
        currentLng = this.getArguments().getDouble("lng");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng currentLocation = new LatLng(currentLat,currentLng);
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("Şuan buradasınız"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12.0f));
        startTimer();
    }

    public void getTaxies(){
        mMap.clear();
        HeyTaksiRest iService = ApiClient.getClient().create(HeyTaksiRest.class);
        Call<BaseReturn<List<ActiveLocationInfo>>> call = iService.activeLocationInfoFindAll();
        call.enqueue(new Callback<BaseReturn<List<ActiveLocationInfo>>>() {
            @Override
            public void onResponse(Call<BaseReturn<List<ActiveLocationInfo>>> call, Response<BaseReturn<List<ActiveLocationInfo>>> response) {
                BaseReturn<List<ActiveLocationInfo>> result = response.body();
                if (result != null && result.data != null && result.data.size() > 0){
                    List<ActiveLocationInfo> list = result.data;
                    for (int i = 0; i < list.size(); i++) {
                        ActiveLocationInfo info = list.get(i);
                        LatLng latLng = new LatLng(info.latitude,info.longitude);
                        mMap.addMarker(new MarkerOptions().position(latLng).title(info.driverId+"").icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("taxi",100,60))));
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseReturn<List<ActiveLocationInfo>>> call, Throwable t) {

            }
        });

    }

    public Bitmap resizeBitmap(String drawableName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier(drawableName, "drawable", getActivity().getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 0, 20000);
    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        getTaxies();
                    }
                });
            }
        };
    }
}
