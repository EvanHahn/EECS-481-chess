package com.eecs481.chess;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ask.scanninglibrary.ASKActivity;

import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * 
 * 
 * @author Jake Korona
 */
public class FriendsActivity extends ASKActivity {

   //////////////////////////////////////////////////////////////////////////
   // Public methods
   //////////////////////////////////////////////////////////////////////////

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_friends);

      m_activityContext = this;
      m_friends = ParseUser.getCurrentUser().getList("friends");

      TextView searchBox = (TextView) findViewById(R.id.searchBox);
      final String searchName = searchBox.getText().toString();

      Button searchButton = (Button) findViewById(R.id.searchBtn);
      searchButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            if (searchName.isEmpty())
               return;

            Log.i("FriendsActivity", "Searching for user: " + searchName);
         }
      });

      ListView friendsListView = (ListView) findViewById(R.id.friendsListView);
      FriendsListAdapter adapter = new FriendsListAdapter(m_activityContext, friendsListView);

      adapter.setList(m_friends);

      friendsListView.setAdapter(adapter);

      friendsListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        	 
        	Intent intent = new Intent(m_activityContext, GameActivity.class);
        	 
		    ArrayList<String> gameParams = new ArrayList<String>();
		    ParseObject game = new ParseObject(Consts.GAME_OBJECT);
		    game.put(Consts.P1_FIELD, ParseUser.getCurrentUser().getUsername());
		    String opponent = (String)arg0.getItemAtPosition(position);
		    game.put(Consts.P2_FIELD, opponent);
		    game.put(Consts.STATUS_FIELD, ParseUser.getCurrentUser().getUsername());
		    game.put(Consts.CUR_GAME_FIELD, Consts.NEW_BOARD);
		    game.saveInBackground();
			    
		    gameParams.add(Consts.NETWORK);
		    gameParams.add(game.getObjectId());
		    gameParams.add(game.getString(Consts.P1_FIELD));
		    gameParams.add(game.getString(Consts.P2_FIELD));
		    gameParams.add(game.getString(Consts.STATUS_FIELD));
		    gameParams.add(game.getString(Consts.CUR_GAME_FIELD));
		    		    
		    intent.putExtra(Consts.GAME_PARAMS, gameParams);

            startActivity(intent);
         }
      });

   }

   //////////////////////////////////////////////////////////////////////////
   // Non-public methods
   //////////////////////////////////////////////////////////////////////////

   private void addFriend(String name) {
      ParseUser user = ParseUser.getCurrentUser();
      user.add("friends", name);
      user.saveInBackground();
   }

   private void saveFriends(List<String> friends) {
      ParseUser user = ParseUser.getCurrentUser();
      user.addAllUnique("friends", friends);
      user.saveInBackground();
   }

   //////////////////////////////////////////////////////////////////////////
   // Non-public fields
   //////////////////////////////////////////////////////////////////////////

   /** The activity. */
   private ASKActivity m_activityContext;

   /** The list of the current user's friends. */
   private List<String> m_friends;
}
