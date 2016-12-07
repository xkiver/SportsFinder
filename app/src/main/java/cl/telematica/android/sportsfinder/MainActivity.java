package cl.telematica.android.sportsfinder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Toast;
import cl.telematica.android.sportsfinder.Model.Place;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
public class MainActivity extends AppCompatActivity implements
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener{

    private GoogleMap mMap;

    private Realm realm;

    private static final String TAG ="Error" ;

    private CheckBox mFutbolCheckbox;

    private CheckBox mBasquetballCheckbox;

    private CheckBox mTennisCheckbox;

    private CheckBox mBicicletaCheckbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        realm = Realm.getDefaultInstance();
        mFutbolCheckbox = (CheckBox) findViewById(R.id.traffic);
        mBasquetballCheckbox = (CheckBox) findViewById(R.id.my_location);
        mTennisCheckbox = (CheckBox) findViewById(R.id.buildings);
        mBicicletaCheckbox = (CheckBox) findViewById(R.id.indoor);
        consultaJson();
    }

    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onClearMap(View view) {
        if (!checkReady()) {
            return;
        }
        mMap.clear();
        mFutbolCheckbox.setChecked(false);
        mBasquetballCheckbox.setChecked(false);
        mTennisCheckbox.setChecked(false);
        mBicicletaCheckbox.setChecked(false);
    }

    public void onFutbolToggled(View view) {
        updateFutbol();
    }
    private void updateFutbol() {
        if (!checkReady()) {
            return;
        }
        mFutbolCheckbox.isChecked();
        RealmResults<Place> results1 = realm.where(Place.class)
                .equalTo("imagen", "soccer")
                .findAll();

        for (int i = 0; i < results1.size(); i++) {
            Place u = results1.get(i);

            addMarkersToMap(u.getName().toString(), u.getDescripction().toString(), u.getImagen().toString(),
                    Double.parseDouble(String.valueOf(u.getLatt())), Double.parseDouble(String.valueOf(u.getLongi())));

        }
    }

    public void onBasquetballToggled(View view) {
        updateBasquetball();
    }
    private void updateBasquetball() {
        if (!checkReady()) {
            return;
        }
        mFutbolCheckbox.isChecked();
        RealmResults<Place> results2 = realm.where(Place.class)
                .equalTo("imagen", "basquetball")
                .findAll();

        for (int i = 0; i < results2.size(); i++) {
            Place u = results2.get(i);

            addMarkersToMap(u.getName().toString(), u.getDescripction().toString(), u.getImagen().toString(),
                    Double.parseDouble(String.valueOf(u.getLatt())), Double.parseDouble(String.valueOf(u.getLongi())));

        }
    }

    public void onTennisToggled(View view) {
        updateTennis();
    }
    private void updateTennis() {
        if (!checkReady()) {
            return;
        }
        mFutbolCheckbox.isChecked();
        RealmResults<Place> results3 = realm.where(Place.class)
                .equalTo("imagen", "tenis")
                .findAll();

        for (int i = 0; i < results3.size(); i++) {
            Place u = results3.get(i);

            addMarkersToMap(u.getName().toString(), u.getDescripction().toString(), u.getImagen().toString(),
                    Double.parseDouble(String.valueOf(u.getLatt())), Double.parseDouble(String.valueOf(u.getLongi())));

        }
    }

    public void onBicicletaToggled(View view) {
        updateBicicleta();
    }
    private void updateBicicleta() {
        if (!checkReady()) {
            return;
        }
        mFutbolCheckbox.isChecked();
        RealmResults<Place> results4 = realm.where(Place.class)
                .equalTo("imagen", "bici")
                .findAll();

        for (int i = 0; i < results4.size(); i++) {
            Place u = results4.get(i);

            addMarkersToMap(u.getName().toString(), u.getDescripction().toString(), u.getImagen().toString(),
                    Double.parseDouble(String.valueOf(u.getLatt())), Double.parseDouble(String.valueOf(u.getLongi())));

        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }
        mMap.setOnInfoWindowClickListener(this);

        //showdata();
        updateFutbol();
        updateBasquetball();
        updateTennis();
        updateBicicleta();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Localizandote!", Toast.LENGTH_SHORT).show();
        return false;
    }


    private void showdata(){

        RealmResults<Place> results = realm.where(Place.class)
                //.equalTo("imagen", "soccer")
                //.or()
                //.equalTo("imagen", "tenis")   FILTRAJE
                .findAll();

        for (int i = 0; i < results.size(); i++) {
            Place u = results.get(i);

            addMarkersToMap(u.getName().toString(), u.getDescripction().toString(), u.getImagen().toString(),
                    Double.parseDouble(String.valueOf(u.getLatt())),Double.parseDouble(String.valueOf(u.getLongi())));

        }
    }


    private void addMarkersToMap(String a, String b, String c, Double d, Double e) {

        LatLng test = new LatLng(d, e);
        Marker tempo;

        switch (c) {
            case "soccer":
                tempo = mMap.addMarker(new MarkerOptions()
                        .position(test)
                        .title(a)
                        .snippet(b)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer)));
                break;
            case "tenis":
                tempo = mMap.addMarker(new MarkerOptions()
                        .position(test)
                        .title(a)
                        .snippet(b)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.tenis)));
                break;

            case "basquetball":
                tempo = mMap.addMarker(new MarkerOptions()
                        .position(test)
                        .title(a)
                        .snippet(b)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.basquetball)));
                break;
            case "bici":
                tempo = mMap.addMarker(new MarkerOptions()
                        .position(test)
                        .title(a)
                        .snippet(b)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.bici)));
                break;
        }

    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Permisos negados. Configure los permisos de la app y vuelva a intentar.", Toast.LENGTH_LONG).show();
                }
                return;
            }
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

        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(this, "Info Window click", Toast.LENGTH_SHORT).show();
        //Intent i = new Intent(MainActivity.this,InfoActivity.class);
        //startActivity(i);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    public void consultaJson() {

        //Definimos un String con la URL del End-point
        String url = "http://www.mocky.io/v2/58470cfb3f0000380efe694e";

        //Instanciamos un objeto RequestQueue el cual se encarga de gestionar la cola de peticiones
        RequestQueue queue = Volley.newRequestQueue(this);

        //Armamos una peticion de tipo JSONArray por que es un JsonArray lo que obtendremos como resultado
        JsonArrayRequest aRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse");
                //Obtenemos un JSONArray como respuesta
                if (response != null && response.length() > 0) {
                    //Iteramos con el JSONArray
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject p = (JSONObject) response.get(i);
                            if (p != null) {

                                realm.beginTransaction();
                                Place places = realm.createObject(Place.class); // Create a new object
                                places.setName(p.getString("Nombre"));
                                places.setDescripction(p.getString("Descripcion"));
                                places.setImagen(p.getString("imagen"));
                                places.setLatt(Double.parseDouble(String.valueOf(p.getString("latitud"))));
                                places.setLongi(Double.parseDouble(String.valueOf(p.getString("longitud"))));
                                realm.commitTransaction();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse");
            }
        });

        //Agregamos la petición de tipo JSON a la cola
        queue.add(aRequest);
    }


}
