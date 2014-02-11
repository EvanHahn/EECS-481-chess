package com.eecs481.chess;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.WebView;

public class GameActivity extends Activity {

	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		//this is an example of how you get access to the current games parse object
		//when in the game activity
		//setContentView(R.layout.activity_game);
		
		//TextView testing = (TextView)findViewById(R.id.game_test);
		//String demo = Homescreen.active_game.getString(Consts.P1_FIELD)
				//+ " " + Homescreen.active_game.getString(Consts.P2_FIELD)
				//+ " " + Homescreen.active_game.getString(Consts.STATUS_FIELD);
		//testing.setText(demo);

		setContentView(R.layout.game_view);

		webView = (WebView) findViewById(R.id.game_view);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl("file:///android_asset/game/index.html");

		setTitle("Pass-and-play");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
