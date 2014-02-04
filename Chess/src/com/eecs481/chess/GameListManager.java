package com.eecs481.chess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;


public class GameListManager {
	
	public static final int GAMES_PER_PAGE = 10;
	
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
				setPage(mCurPage + 1);
			} 
		});
		
		((Button) mActivity.findViewById(R.id.previousPageButton)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setPage(mCurPage - 1);
			} 
		});
		
		new GetGamesPagesTask().execute();

	}
	
	private class GetGamesPagesTask extends AsyncTask<Void, Void, Void> {
	     protected Void doInBackground(Void... args) {
	    	 mGames.clear();
	 		 queryForGames();
	    	 return null;
	     }
	     
	     protected void onPostExecute(Void arg) {
	    	 setUpPages();
	     }
	 }	
	
	private void setPage(int pageNum) {
		if (pageNum < 0) {
			pageNum = 0;
		} else {
			pageNum = Math.min(mPages.size() - 1, pageNum);
		}
		
		mCurPage = pageNum;
		
		mAdapter.clear();
		if (pageNum < mPages.size()) {
			mAdapter.addAll(mPages.get(pageNum));
		}
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

		
		mFullList.addAll(mGames.values());
		Collections.sort(mFullList);
	}
	
	private void setUpPages() {
		mPages = chopList(mFullList, GAMES_PER_PAGE);
		
		if (!mPages.isEmpty()) {
	    	 mAdapter.clear();
	 		 mAdapter.addAll(mPages.get(0));
			 mAdapter.notifyDataSetChanged();
		}
	}
	
	private <T> List<List<T>> chopList(List<T> list, final int L) {
	    List<List<T>> parts = new ArrayList<List<T>>();
	    final int N = list.size();
	    for (int i = 0; i < N; i += L) {
	        parts.add(new ArrayList<T>(
	            list.subList(i, Math.min(N, i + L)))
	        );
	    }
	    return parts;
	}
	
	
	private GamesViewAdapter mAdapter;
	private Homescreen mActivity;
	private ListView mListView;
	private int mCurPage = 0;
	private Hashtable<String, String> mGames = new Hashtable<String, String>();
	private ArrayList<String> mFullList = new ArrayList<String>();
	private List<List<String>> mPages;
}
