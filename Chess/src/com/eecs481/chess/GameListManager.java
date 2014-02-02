package com.eecs481.chess;

import java.util.Collection;
import java.util.Hashtable;
import java.util.TreeSet;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;


public class GameListManager {
	
	public GameListManager(final Homescreen homeScreen) {
		mActivity = homeScreen;
		mListView = ((ListView) mActivity.findViewById(R.id.gamesListView));
		mAdapter = new GamesViewAdapter(mActivity, mListView);
		
		mListView.setAdapter(mAdapter);
		
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
			    Intent intent = new Intent(mActivity, GameActivity.class);
			    mActivity.startActivity(intent);
			}
		});
		
		((Button) mActivity.findViewById(R.id.nextPageButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mAdapter.nextPage();
			} 
		});
		
		((Button) mActivity.findViewById(R.id.previousPageButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListView.smoothScrollToPosition(mListView.getFirstVisiblePosition());
			} 
		});

		
		getGames();
	}
	
	private void getGames() {
		mGames.clear();
		mAdapter.clear();
		queryForGames();
		mAdapter.addAll(getSortedGames());
		mAdapter.notifyDataSetChanged();
	}
	
	private void queryForGames() {
		//contains fake data right now
		
		mGames.put("game1", "game1_name");
		mGames.put("game2", "game2_name");
		mGames.put("game3", "game3_name");
		mGames.put("game4", "game4_name");
		mGames.put("game5", "game5_name");
		mGames.put("game6", "game6_name");
		mGames.put("game7", "game7_name");
		mGames.put("game8", "game8_name");
		mGames.put("game9", "game9_name");
		mGames.put("game10", "game10_name");
		mGames.put("game11", "game11_name");
		mGames.put("game12", "game12_name");
		mGames.put("game13", "game13_name");
		mGames.put("game14", "game14_name");
		mGames.put("game15", "game15_name");
		mGames.put("game16", "game16_name");
		mGames.put("game17", "game17_name");
		mGames.put("game18", "game18_name");
		mGames.put("game19", "game19_name");
		mGames.put("game20", "game20_name");
	}
	
	//this will end up sorting by some other criteria
	private Collection<String> getSortedGames() {
		
		TreeSet<String> result = new TreeSet<String>();
				
		result.addAll(mGames.values());
		
		return result;
	}
	
	
	private GamesViewAdapter mAdapter;
	private Homescreen mActivity;
	private ListView mListView;
	private Hashtable<String, String> mGames = new Hashtable<String, String>();
}
