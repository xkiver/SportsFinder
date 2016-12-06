package cl.telematica.android.sportsfinder;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by luxor on 05-12-16.
 */

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Intent i = new Intent(ImageActivity.this, MainActivity.class);
                startActivity(i);
            }
        }, 1000);

    }
}
