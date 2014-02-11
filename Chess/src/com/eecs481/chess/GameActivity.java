package com.eecs481.chess;

import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

public class GameActivity extends Activity {

	private WebView webView;

	private class Ferry { // the "bridge" between real Android and JavaScript

		private Context context;

		Ferry(Context c) {
			context = c;
		}

		@JavascriptInterface
		public String getCurrentUser() {
			return ParseUser.getCurrentUser().getUsername();
		}

		@JavascriptInterface
		public String getPlayer1() {
			return Homescreen.active_game.getString(Consts.P1_FIELD);
		}

		@JavascriptInterface
		public String getPlayer2() {
			return Homescreen.active_game.getString(Consts.P2_FIELD);
		}

		@JavascriptInterface
		public Boolean getIsPassAndPlay() {
			return false;
		}

		@JavascriptInterface
		public String getBoardState() {
			return "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2";
		}

	}

	@Override
	@SuppressLint("SetJavaScriptEnabled")
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.game_view);

		webView = (WebView) findViewById(R.id.game_view);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new Ferry(this), "ferry");
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
