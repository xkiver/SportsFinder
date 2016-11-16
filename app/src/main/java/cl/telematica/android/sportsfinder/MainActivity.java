package cl.telematica.android.sportsfinder;

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
        NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback{

    private GoogleMap mMap;

    private LatLng CanchaUSM = new LatLng(-33.035753, -71.592886);

    private LatLng CanchaBas = new LatLng(-33.035333, -71.592913);

    private LatLng CanchaFut = new LatLng(-33.034713, -71.594236);

    private LatLng CanchaTennis = new LatLng(-33.035222, -71.596859);



    private Marker mCanchaUSM;

    private Marker mCanchaBas;

    private Marker mCanchaFut;

    private Marker mCanchaTennis;

    private Realm realm;

    private static final String TAG ="Error" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);


        realm = Realm.getDefaultInstance();
        // ... Do something ...
        File outFile = this.getDatabasePath("default.realm");
        String outFileName = outFile.getPath();
        System.out.println(outFile);

        consultaJson();
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


        savedata();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Buscando Localización", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void savedata(){

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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

        }
        else if (id == R.id.nav_gallery) {

        }
        else if (id == R.id.nav_slideshow) {

        }
        else if (id == R.id.nav_manage) {

        }
        else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


    public void consultaJson() {

        //Definimos un String con la URL del End-point
        String url = "http://www.mocky.io/v2/582b54b4280000621953c490";

        //Hacemos uso de Volley para consumir el End-point
        //myDataset = new ArrayList<Lista>();



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
                                //Armamos un objeto Photo con el Title y la URL de cada JSONObject
                                //Place lugar = new Place();

                                /*
                                String nombre = p.getString("Nombre");
                                String descripcion = p.getString("Descripcion");
                                String imagen = p.getString("imagen");
                                double latitud = Double.parseDouble(String.valueOf(p.getString("latitud")));
                                double longitud = Double.parseDouble(String.valueOf(p.getString("longitud")));

                                addMarkersToMap(nombre,descripcion,imagen,latitud,longitud);
                                //System.out.println(nombre);
                                //System.out.println(descripcion);
                                //System.out.println(imagen);
                                //System.out.println(latitud);
                                //System.out.println(longitud);
                              */
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
