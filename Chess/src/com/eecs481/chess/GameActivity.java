package com.eecs481.chess;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class GameActivity extends Activity {

	private WebView webView;
	private ArrayList<String> mGameParams;
	private Boolean pnpGame = true;

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
		public String getObjectId() {
			if (!pnpGame) {
				return mGameParams.get(1);
			} else {
				return "";
			}
		}

		@JavascriptInterface
		public String getPlayer1() {
			if (!pnpGame) {
				return mGameParams.get(2);
			} else {
				return "";
			}
		}

		@JavascriptInterface
		public String getPlayer2() {
			if (!pnpGame) {
				return mGameParams.get(3);
			} else {
				return "";
			}
		}
		
		@JavascriptInterface
		public Boolean isMyTurn() {
			return pnpGame || getCurrentUser().equals(getGameStatus());
		}
		
		@JavascriptInterface
		public String getGameStatus() {
			if (!pnpGame) {
				return mGameParams.get(4);
			} else {
				return "";
			}
		}

		@JavascriptInterface
		public Boolean getIsPassAndPlay() {
			return pnpGame;
		}

		@JavascriptInterface
		public String getBoardState() {
			if (!pnpGame) {
				return mGameParams.get(5);
			} else {
				return Consts.NEW_BOARD;
			}
		}
		
		@JavascriptInterface
		public Boolean whiteScanning() {
			if (pnpGame) {
				return mGameParams.get(1).equals(Consts.SCANNING);
			} else {
				return true;
			}
		}
		
		@JavascriptInterface
		public Boolean blackScanning() {
			if (pnpGame) {
				return mGameParams.get(2).equals(Consts.SCANNING);
			} else {
				return true;
			}
		}
		
		@JavascriptInterface
		public void saveBoardState(final String status, final String boardState) {
			
			if (pnpGame) {
				return;
			}
			
			String id = getObjectId();
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery(Consts.GAME_OBJECT);
			query.getInBackground(id, new GetCallback<ParseObject>() {
			  public void done(ParseObject game, ParseException e) {
			    if (e == null) {
			      game.put(Consts.STATUS_FIELD, status);
			      game.put(Consts.CUR_GAME_FIELD, boardState);
			      game.saveInBackground();
			    }
			  }
			});

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
		
		Intent intent = getIntent();
		
		mGameParams = intent.getStringArrayListExtra(Consts.GAME_PARAMS);
		
		if (mGameParams.get(0).equals(Consts.PNP)) {
			setTitle("Pass-and-play");
			pnpGame = true;
		} else {
			setTitle("Network");
			pnpGame = false;
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}

}
