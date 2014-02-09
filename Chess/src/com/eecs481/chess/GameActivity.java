package com.eecs481.chess;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class GameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		
		TextView testing = (TextView)findViewById(R.id.game_test);
		String demo = Homescreen.active_game.getString(Consts.P1_FIELD)
				+ " " + Homescreen.active_game.getString(Consts.P2_FIELD)
				+ " " + Homescreen.active_game.getString(Consts.STATUS_FIELD);
		testing.setText(demo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
