package com.eecs481.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class GameListManager {
	
	public static final int GAMES_PER_PAGE = 10;
	
	public GameListManager(final Homescreen homeScreen) {
		mActivity = homeScreen;
		currentUser = ParseUser.getCurrentUser();

		mListView = ((ListView) mActivity.findViewById(R.id.gamesListView));
		mAdapter = new GamesViewAdapter(mActivity, mListView);
		
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
			    Intent intent = new Intent(mActivity, GameActivity.class);
			    //intent.putExtra(name, value);
			    //Toast.makeText(mActivity, mFullList.get(position - 1).id, Toast.LENGTH_LONG).show();
			    
			    Homescreen.active_game = mGames.get(mFullList.get(position - 1).id);
			    mActivity.startActivity(intent);
			}
		});
		
		findUserGames();

	}
	
	public void findUserGames() {
		if (currentUser == null) {
			return;
		}
		
		ParseQuery<ParseObject> queryPlayer1 = ParseQuery.getQuery(Consts.GAME_OBJECT);
		queryPlayer1.whereEqualTo(Consts.P1_FIELD, currentUser.getUsername());
		
		ParseQuery<ParseObject> queryPlayer2 = ParseQuery.getQuery(Consts.GAME_OBJECT);
		queryPlayer2.whereEqualTo(Consts.P2_FIELD, currentUser.getUsername());
		
		List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
		queries.add(queryPlayer1);
		queries.add(queryPlayer2);
		
		ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
		
		mainQuery.findInBackground(new FindCallback<ParseObject>() {
		  public void done(List<ParseObject> results, ParseException e) {
		    if (e == null) {
		    	mAdapter.clear();
		    	mGames.clear();
		    	mFullList.clear();
		    	setUpGameLists(results);
		    	mAdapter.addAll(mFullList);
		    	mAdapter.notifyDataSetChanged();
		    } else {
		    	System.err.println("error during get games query!");
		    }
		  }
		});
	}
	
	private void setUpGameLists(List<ParseObject> games) {
		
		for (ParseObject pObj : games) {
			mGames.put(pObj.getObjectId(), pObj);
			
			String player1Name = pObj.getString(Consts.P1_FIELD);
			String player2Name = pObj.getString(Consts.P2_FIELD);
			String gameStatus = pObj.getString(Consts.STATUS_FIELD);
			
			if (player1Name.equals(currentUser.getUsername())) {
				mFullList.add(new GameInfo(pObj.getObjectId(), player2Name, gameStatus));
			} else {
				mFullList.add(new GameInfo(pObj.getObjectId(), player1Name, gameStatus));
			}
		}
	}

	
	private GamesViewAdapter mAdapter;
	private Homescreen mActivity;
	private ListView mListView;
	private Hashtable<String, ParseObject> mGames = new Hashtable<String, ParseObject>();
	private List<GameInfo> mFullList = Collections.synchronizedList(new ArrayList<GameInfo>());
	private ParseUser currentUser;
}
