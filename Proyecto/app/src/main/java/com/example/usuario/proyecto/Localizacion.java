package com.example.usuario.proyecto;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.images.ImageRequest;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Localizacion extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener, MapboxMap.OnMapClickListener {
    private MapView mapView;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originPosistion;
    private Point destinoPosition;
    private Marker destinomarker;
    private Button boto;
    private NavigationMapRoute navigationMapRoute;
    private static final String TAG = "Localizacion";
    public static double longitud;
    public static double latitud;
    public static String direccion;
    TextView te;
    private Dialog midialogo;
    private AccessServiceAPI miser;
    public static String telefono;
    public static String id_enfermo;
    public static String imei;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this, getString(R.string.access_token));
        setContentView(R.layout.activity_localizacion);
        mapView = findViewById(R.id.mapav);
        boto = findViewById(R.id.boton);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        te = findViewById(R.id.loca);
       // cogerCalle();
        getCoordenadas();
        te.setText(direccion);
        boto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cogerCalle();
                te.setText(direccion);


                    NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                            .origin(originPosistion)
                            .destination(destinoPosition)
                            .shouldSimulateRoute(false)
                            .build();
                    NavigationLauncher.startNavigation(Localizacion.this, options);


            }
        });


    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onConnected() {

        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            originLocation = location;
            setCamera(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();

        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);


    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map = mapboxMap;
        map.addOnMapClickListener(this);
        enableLocation();
    }

    private void enableLocation() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            inicializa();
            inicializalo();
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);

        }
    }

    @SuppressWarnings("MissingPermission")
    private void inicializa() {
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            originLocation = lastLocation;
            setCamera(lastLocation);
        } else {
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void inicializalo() {
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

    private void setCamera(Location location) {
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13.0));

    }

    @Override
    public void onMapClick(@NonNull LatLng point) {


        point.setLongitude(longitud);
        point.setLatitude(latitud);
       if(localizcionVal(originLocation.getLatitude(),originLocation.getLongitude())) {
            destinomarker = map.addMarker(new MarkerOptions().position(point));
            destinoPosition = Point.fromLngLat(longitud, latitud);
            originPosistion = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
            getRoute(originPosistion, destinoPosition);
            te.setText(direccion);

            boto.setEnabled(true);
            boto.setBackgroundResource(R.color.mapboxBlue);
        }
        else {
            Toast.makeText(getBaseContext(), "Esta opcion solo funciona en dentro de madrid",Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void getRoute(Point origen, Point destino) {

        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origen)
                .destination(destino)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if (response.body() == null) {
                            Log.e(TAG, "NO HAY  RUTA");
                            return;
                        } else if (response.body().routes().size() == 0) {
                            Log.e(TAG, "NO HAY RUTA ");
                            return;
                        }
                        DirectionsRoute currentRoute = response.body().routes().get(0);
                        if (navigationMapRoute != null) {
                            navigationMapRoute.removeRoute();
                        } else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map);
                        }

                        navigationMapRoute.addRoute(currentRoute);


                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG, "ERROR" + t.getMessage());
                    }
                });
    }

    @Override
    @SuppressWarnings("MissingPermission")
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.requestLocationUpdates();
        }
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStart();
        }

        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStop();
        }
        mapView.onDestroy();
    }

    public void getCoordenadas() {
        //Obtener la direccion de la calle a partir de la latitud y la longitud

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> list = geocoder.getFromLocationName(direccion+ ",Madrid,España", 1);//direccion
            if (!list.isEmpty()) {
                Address coordenadas = list.get(0);
                latitud = coordenadas.getLatitude();
                longitud = coordenadas.getLongitude();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public boolean localizcionVal(double latitud,double longitud) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> list = geocoder.getFromLocation(latitud,longitud,1);
            if ((!list.isEmpty() &&( list.get(0).getCountryName().equals("Spain"))||
                    list.get(0).getCountryName().equals("España"))) {
            return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void cogerCalle() {
        direccion = getIntent().getStringExtra("direccion");
        id_enfermo = getIntent().getStringExtra("id_enfermo");
        telefono = getIntent().getStringExtra("telefono");
        imei = getIntent().getStringExtra("imei");
        te.setText(direccion);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), Principal.class);
        i.putExtra("direccion", direccion);
        i.putExtra("telefono", telefono);
        i.putExtra("id_enfermo", id_enfermo);
        i.putExtra("imei", imei);
        startActivity(i);
        finish();
    }
}
