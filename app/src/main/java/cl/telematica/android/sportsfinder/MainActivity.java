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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmResults;
public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback {

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

        addMarkersToMap();
        savedata();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "Buscando Localización", Toast.LENGTH_SHORT).show();
        return false;
    }

    private void savedata() {
        realm.beginTransaction();

        // Create an object
        Place lugar = realm.createObject(Place.class);

        // Set its fields
        lugar.setName("7x7");
        lugar.setDescripction("Cancha Baby Futbol");
        lugar.setImagen("soccer");
        lugar.setLatt(-33.035753);
        lugar.setLongi(-71.592886);

        realm.commitTransaction();
        System.out.println(realm.getPath());

        RealmResults<Place> result2 = realm.where(Place.class)
                .equalTo("name", "7x7")
                .findAll();
        String output = "";
        for(Place place: result2){
            output += place.toString();
        }
        System.out.println(output);


    }

    private void addMarkersToMap() {
        // Uses a colored icon.
        mCanchaUSM = mMap.addMarker(new MarkerOptions()
                .position(CanchaUSM)
                .title("7 x 7")
                .snippet("Cancha de Baby Futbol 7 Jugadores por lado")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer)));

        mCanchaBas = mMap.addMarker(new MarkerOptions()
                .position(CanchaBas)
                .title("Chancha Basketball")
                .snippet("Cancha de Basketball 5 Jugadores por lado")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.basquetball)));

        mCanchaFut = mMap.addMarker(new MarkerOptions()
                .position(CanchaFut)
                .title("Chancha Futbol")
                .snippet("Cancha de Futbol, pasto, 11 Jugadores por lado")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.soccer)));

        mCanchaTennis = mMap.addMarker(new MarkerOptions()
                .position(CanchaTennis)
                .title("Chancha Tenis")
                .snippet("Cancha de Tenis, arcilla")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.tenis)));

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

    private void save_data() {

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                Place user = bgRealm.createObject(Place.class);
                user.setName("John");
                user.setDescripction("john@corporation.com");
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                // Transaction was a success.
                Log.v("database", "<< stored ok >>");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                // Transaction failed and was automatically canceled.
                Log.e("database", error.getMessage());
            }
        });

        File outFile = this.getDatabasePath("default.realm");
        String outFileName = outFile.getPath();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }


}
