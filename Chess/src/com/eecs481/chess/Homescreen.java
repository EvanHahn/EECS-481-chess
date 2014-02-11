package com.eecs481.chess;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import ask.scanninglibrary.ASKActivity;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class Homescreen extends ASKActivity {
	
   public static ParseObject active_game;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	  setContentView(R.layout.activity_homescreen);
	  setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
	
	  m_activityContext = this; 
	  m_gameList = new GameListManager(this);
	  m_newGameButton = (Button) findViewById(R.id.newGameButton);
	  
	  
	  m_newGameButton.setOnClickListener(new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(Homescreen.this, NewGameActivity.class);
			startActivity(intent);
		}
	  });
   }
   
   @Override
   protected void onResume() {
	   super.onResume();
	   
	   m_gameList.findUserGames();
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
   private Button m_newGameButton;
   private GameListManager m_gameList;
}
