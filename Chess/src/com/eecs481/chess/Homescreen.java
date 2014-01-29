package com.eecs481.chess;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.parse.Parse;

public class Homescreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);
        Parse.initialize(this, "4SV3X5Flt3tqhr87pM29xI36jKYtUWnZWBBI70iH", "vxkrbGSjbstIO1UjUg6PGicEUCSvcEVo9p5kjgZ4");
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homescreen, menu);
        return true;
    }
    
}
