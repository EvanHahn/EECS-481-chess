package com.eecs481.chess;

import java.util.ArrayList;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

   public static String BOARD_STATE_PREF = "gc_boardState";

   private WebView webView;
   private ArrayList<String> mGameParams;
   private boolean pnpGame = true;
   private Intent backbuttonIntent;
   private String m_pnpBoardState;

   private class Ferry { // the "bridge" between real Android and JavaScript

      private final Context context;
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
         }
         else {
            return "";
         }
      }

      @JavascriptInterface
      public String getPlayer1() {
         if (!pnpGame) {
            return mGameParams.get(2);
         }
         else {
            return "";
         }
      }

      @JavascriptInterface
      public String getPlayer2() {
         if (!pnpGame) {
            return mGameParams.get(3);
         }
         else {
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
         }
         else {
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
         }
         else {
            return m_pnpBoardState;
         }
      }

      @JavascriptInterface
      public boolean whiteScanning() {
         if (pnpGame) {
            return mGameParams.get(1).equals(Consts.SCANNING);
         }
         else {
            return true;
         }
      }

      @JavascriptInterface
      public boolean blackScanning() {
         if (pnpGame) {
            return mGameParams.get(2).equals(Consts.SCANNING);
         }
         else {
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
            SharedPreferences gameState = getSharedPreferences(BOARD_STATE_PREF, 0);
            SharedPreferences.Editor editor = gameState.edit();
            editor.putString("boardState", boardState);

            // Commit the edits!
            editor.commit();
            return;
         }

         String id = getObjectId();

         ParseQuery<ParseObject> query = ParseQuery.getQuery(Consts.GAME_OBJECT);
         query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject game, ParseException e) {
               if (e == null) {
                  if (game == null) {
                     makeToast("Game not found!");
                     return;
                  }
                  game.put(Consts.STATUS_FIELD, status);
                  game.put(Consts.CUR_GAME_FIELD, boardState);
                  game.saveInBackground();
               }
               else {
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

      getActionBar().hide();

      backbuttonIntent = new Intent(this, Homescreen.class);

      mGameParams = getIntent().getStringArrayListExtra(Consts.GAME_PARAMS);

      if (mGameParams.get(0).equals(Consts.PNP)) {
         pnpGame = true;

         SharedPreferences gameState = getSharedPreferences(BOARD_STATE_PREF, 0);
         m_pnpBoardState = gameState.getString("boardState", Consts.NEW_BOARD);
      }
      else
         pnpGame = false;

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

   private void makeToast(String message) {
      Toast.makeText(GameActivity.this, message, Toast.LENGTH_SHORT).show();
   }

}
