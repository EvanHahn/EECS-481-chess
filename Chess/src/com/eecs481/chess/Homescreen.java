package com.eecs481.chess;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.parse.ParseUser;

public class Homescreen extends Activity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_homescreen);

      m_activityContext = this;
   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.homescreen_menu, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.action_settings:
            return true;
         case R.id.action_logout:
            ParseUser.logOut();
            m_activityContext.finish();
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }

   private Activity m_activityContext;
}
