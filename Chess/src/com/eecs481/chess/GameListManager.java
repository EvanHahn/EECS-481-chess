package com.eecs481.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class GameListManager {

   public GameListManager(final Homescreen homeScreen) {
      mActivity = homeScreen;
      currentUser = ParseUser.getCurrentUser();

      mListView = ((ListView) mActivity.findViewById(R.id.gamesListView));
      mAdapter = new GamesViewAdapter(mActivity, mListView);

      mListView.setAdapter(mAdapter);

      mListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            ParseObject game = mGames.get(mFullList.get(position - 1).id);
            ArrayList<String> gameParams = Utility.getGameParams(game);

            Intent intent = new Intent(mActivity, GameActivity.class);
            intent.putExtra(Consts.GAME_PARAMS, gameParams);
            mActivity.startActivity(intent);
         }
      });

      findUserGames();

   }

   public void findUserGames() {
      if (currentUser == null) {
         return;
      }

      ParseQuery<ParseObject> gameQuery = getGameQuery();

      gameQuery.findInBackground(new FindCallback<ParseObject>() {
         @Override
         public void done(List<ParseObject> results, ParseException e) {
            if (e == null) {
               mAdapter.clear();
               setUpGameLists(results);
               mAdapter.addAll(mFullList);
               mAdapter.notifyDataSetChanged();
            } else {
               System.err.println("error during get games query!");
            }
         }
      });
   }

   public void refreshUserGames() {
      if (currentUser == null) {
         return;
      }

      ParseQuery<ParseObject> gameQuery = getGameQuery();

      gameQuery.findInBackground(new FindCallback<ParseObject>() {
         @Override
         public void done(List<ParseObject> results, ParseException e) {
            if (e == null) {
               if (gamesChanged(results)) {
                  mAdapter.clear();
                  setUpGameLists(results);
                  mAdapter.addAll(mFullList);
                  mAdapter.notifyDataSetChanged();
               }
            } else {
               System.err.println("error during get games query!");
            }
         }
      });
   }

   private void setUpGameLists(List<ParseObject> games) {
      mGames.clear();
      mFullList.clear();

      for (ParseObject pObj : games) {
         mGames.put(pObj.getObjectId(), pObj);

         String player1Name = pObj.getString(Consts.P1_FIELD);
         String player2Name = pObj.getString(Consts.P2_FIELD);
         String gameStatus = pObj.getString(Consts.STATUS_FIELD);

         String displayStatus = (gameStatus.equals(currentUser.getUsername())) ? "Your Turn" : "Opponent's Turn";

         if (player1Name.equals(currentUser.getUsername())) {
            mFullList.add(new GameInfo(pObj.getObjectId(), player2Name, displayStatus));
         } else {
            mFullList.add(new GameInfo(pObj.getObjectId(), player1Name, displayStatus));
         }
      }
      Collections.sort(mFullList, new StatusComparator());
   }

   private Boolean gamesChanged(List<ParseObject> games) {
      for (ParseObject pObj : games) {
         String id = pObj.getObjectId();
         String newGameStatus = pObj.getString(Consts.STATUS_FIELD);

         if (!(mGames.get(id).getString(Consts.STATUS_FIELD).equals(newGameStatus))) {
            return true;
         }
      }

      return false;
   }

   private ParseQuery<ParseObject> getGameQuery() {
      ParseQuery<ParseObject> queryPlayer1 = ParseQuery.getQuery(Consts.GAME_OBJECT);
      queryPlayer1.whereEqualTo(Consts.P1_FIELD, currentUser.getUsername());

      ParseQuery<ParseObject> queryPlayer2 = ParseQuery.getQuery(Consts.GAME_OBJECT);
      queryPlayer2.whereEqualTo(Consts.P2_FIELD, currentUser.getUsername());

      List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
      queries.add(queryPlayer1);
      queries.add(queryPlayer2);

      return ParseQuery.or(queries);
   }

   private final GamesViewAdapter mAdapter;
   private final Homescreen mActivity;
   private final ListView mListView;
   private final Hashtable<String, ParseObject> mGames = new Hashtable<String, ParseObject>();
   private final List<GameInfo> mFullList = Collections.synchronizedList(new ArrayList<GameInfo>());
   private final ParseUser currentUser;
}
