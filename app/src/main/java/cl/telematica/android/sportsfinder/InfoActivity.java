package cl.telematica.android.sportsfinder;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by luxor on 06-12-16.
 */

public class InfoActivity extends MainActivity{

    TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layer);
        Intent i = getIntent();
        Bundle extras = i.getExtras();
        System.out.println(extras.getString("description"));
        info = (TextView) findViewById(R.id.textView2);
        info.setText(extras.getString("description"));

    }
}
