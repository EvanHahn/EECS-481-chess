package com.eecs481.chess;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class GameActivity extends Activity {

	private WebView webView;
	private ArrayList<String> mGameParams;
	private boolean pnpGame = true;
	private Intent backbuttonIntent;

	private class Ferry { // the "bridge" between real Android and JavaScript

		private Context context;
		public boolean needsToClick;

		Ferry(Context c) {
			context = c;
			needsToClick = false;
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
		public boolean isMyTurn() {
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
		public boolean getIsPassAndPlay() {
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
		public boolean whiteScanning() {
			if (pnpGame) {
				return mGameParams.get(1).equals(Consts.SCANNING);
			} else {
				return true;
			}
		}
		
		@JavascriptInterface
		public boolean blackScanning() {
			if (pnpGame) {
				return mGameParams.get(2).equals(Consts.SCANNING);
			} else {
				return true;
			}
		}
		
		@JavascriptInterface
		public void backButton() {
            startActivity(backbuttonIntent);
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
			    	if (game == null) {
			    		makeToast("Game not found!");
			    		return;
			    	}
			    	game.put(Consts.STATUS_FIELD, status);
			    	game.put(Consts.CUR_GAME_FIELD, boardState);
			    	game.saveInBackground();
			    } else {
			    	makeToast("Parse query exception");
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
		
		backbuttonIntent = new Intent(this, Homescreen.class);
		
		Intent intent = getIntent();
		
		mGameParams = intent.getStringArrayListExtra(Consts.GAME_PARAMS);
		
		if (mGameParams.get(0).equals(Consts.PNP)) {
			setTitle("Pass-and-play");
			pnpGame = true;
		} else {
			setTitle("Network");
			pnpGame = false;
		}
		
		final Ferry ferry = new Ferry(this);
		
		webView = (WebView) findViewById(R.id.game_view);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(ferry, "ferry");
		webView.loadUrl("file:///android_asset/game/index.html");
		
		webView.requestFocus(View.FOCUS_DOWN);
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
            	boolean shouldClick = (event.getAction() == MotionEvent.ACTION_UP);
            	if (shouldClick) {
            		webView.loadUrl("javascript:ask.click();");
            	}
				return false;
            }
        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game, menu);
		return true;
	}
	
	private void makeToast(String message) {
		Toast.makeText(GameActivity.this, message, Toast.LENGTH_SHORT).show();
	}

}
