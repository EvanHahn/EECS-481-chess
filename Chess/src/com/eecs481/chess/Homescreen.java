package com.eecs481.chess;

import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import ask.scanninglibrary.ASKActivity;

import com.parse.ParseUser;

/**
 * This activity lists all of the user's active games. It also allows the user
 * to start a new offline pass & play game or an online network game.
 * 
 * @author Will Wood
 * @author Jake Korona
 */
public class Homescreen extends ASKActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_homescreen);
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

      m_activityContext = this;
      m_gameList = new GameListManager(this);

      Button passPlayButton = (Button) findViewById(R.id.passPlayButton);
      Button netPlayButton = (Button) findViewById(R.id.netPlayButton);

      passPlayButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
        	//to be used in the final release 
        	//Intent intent = new Intent(m_activityContext, GameSettingsPnp.class);
        	 
            ArrayList<String> gameParams = new ArrayList<String>();
            gameParams.add(Consts.PNP);
            
            Intent intent = new Intent(m_activityContext, GameActivity.class);
            intent.putExtra(Consts.GAME_PARAMS, gameParams);
            startActivity(intent);
         }
      });

      netPlayButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(m_activityContext, FriendsActivity.class));
         }
      });
      
      refresh = new Runnable() {
		@Override
		public void run() {
			m_gameList.refreshUserGames();
			handler.postDelayed(this, delay);
		}
      };
      
      //start thread to refresh after delay
      handler.postDelayed(refresh, delay);
   }

   @Override
   protected void onResume() {
      super.onResume();
      m_gameList.findUserGames();
      handler.postDelayed(refresh, delay);
   }
   
   @Override
   protected void onPause() {
	   super.onPause();
	   handler.removeCallbacks(refresh);
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
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
   
   /** The activity. */
   private ASKActivity m_activityContext;

   /** Manager for the games list. */
   private GameListManager m_gameList;
   
   private Handler handler = new Handler();
   private Runnable refresh;
   private long delay = 180000; //3 minutes
}
