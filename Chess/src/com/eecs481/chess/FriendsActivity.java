package com.eecs481.chess;

import java.util.ArrayList;
import java.util.Collections;
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
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * The friends list activity.
 * 
 * @author Jake Korona
 */
public class FriendsActivity extends ASKActivity {

   //////////////////////////////////////////////////////////////////////////
   // Activity overrides
   //////////////////////////////////////////////////////////////////////////

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_friends);

      m_activityContext = this;
      m_user = ParseUser.getCurrentUser();

      mSearchBox = (TextView) findViewById(R.id.searchBox);
      mSearchBox.setText("");

      Button searchButton = (Button) findViewById(R.id.searchBtn);
      searchButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            final String searchName = mSearchBox.getText().toString();
            if (searchName.isEmpty())
               return;

            mSearchBox.setText("");
            searchForUser(searchName);

            Log.i("FriendsActivity", "Searching for user: " + searchName);
         }
      });

      Button backButton = (Button) findViewById(R.id.homescreenButton);
      backButton.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            startActivity(new Intent(FriendsActivity.this, Homescreen.class));
         }
      });

      ListView friendsListView = (ListView) findViewById(R.id.friendsListView);

      mAdapter = new FriendsListAdapter(m_activityContext, friendsListView);
      mAdapter.setList(m_friends);
      friendsListView.setAdapter(mAdapter);

      refreshDisplayedFriends();

      friendsListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            String opponent = (String)arg0.getItemAtPosition(position);
            if (opponent.isEmpty() || opponent == null)
               return;

            ParseObject game = makeNewGame(opponent);
            ArrayList<String> gameParams = Utility.getGameParams(game);

            Intent intent = new Intent(m_activityContext, GameActivity.class);
            intent.putExtra(Consts.GAME_PARAMS, gameParams);

            startActivity(intent);
         }
      });
   }

   //////////////////////////////////////////////////////////////////////////
   // Non-public methods
   //////////////////////////////////////////////////////////////////////////

   /**
    * Constructs and saves a new game.
    * 
    * @param opponent the opponent's username
    * @return a new game
    */
   private ParseObject makeNewGame(final String opponent) {
      ParseObject game = new ParseObject(Consts.GAME_OBJECT);
      game.put(Consts.P1_FIELD, m_user.getUsername());
      game.put(Consts.P2_FIELD, opponent);
      game.put(Consts.STATUS_FIELD, m_user.getUsername());
      game.put(Consts.CUR_GAME_FIELD, Consts.NEW_BOARD);
      try {
         game.save();
      } catch (ParseException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

      return game;
   }

   /**
    * Searches Parse for a user.
    * 
    * @param username the username
    */
   private void searchForUser(final String username) {
      if (username.isEmpty() || username == null)
         return;

      if (m_friends.contains(username)) {
         makeToast("That person is already your friend!");
         return;
      }

      if (m_user.getUsername().equals(username)) {
         makeToast("Cannot add self as friend!");
         return;
      }

      ParseQuery<ParseUser> query = ParseUser.getQuery();
      query.whereEqualTo("username", username);
      query.findInBackground(new FindCallback<ParseUser>() {
         @Override
         public void done(List<ParseUser> objects, ParseException e) {
            if (e == null) {
               if (!objects.isEmpty()) {
                  addFriend(objects.get(0));
                  refreshDisplayedFriends();
                  makeToast("New friend " + objects.get(0).getUsername() + " added!");
               }
               else
                  makeToast("No user of that name!");
            }
            else
               makeToast("Something went wrong!");
         }
      });
   }

   /**
    * Refreshes the list of friends.
    */
   private void refreshDisplayedFriends() {
      List<String> friends = m_user.getList(Consts.USER_FRIENDS);
      if (friends != null) {
         m_friends.clear();
         m_friends.addAll(friends);
         Collections.sort(m_friends);

         mAdapter.clear();
         mAdapter.setList(m_friends);
         mAdapter.notifyDataSetChanged();
      }
   }

   /**
    * Adds a user to the current user's friends list and vice versa.
    * 
    * @param friend the friend to add
    */
   private void addFriend(final ParseUser friend) {
      ParseUser user = m_user;
      user.add(Consts.USER_FRIENDS, friend.getUsername());
      friend.add(Consts.USER_FRIENDS, m_user.getUsername());

      user.saveInBackground(new SaveCallback() {
         @Override
         public void done(ParseException e) {
            friend.saveInBackground();
         }
      });
   }

   /**
    * Constructs a short toast message.
    * 
    * @param message the message
    */
   private void makeToast(String message) {
      Toast.makeText(m_activityContext, message, Toast.LENGTH_SHORT).show();
   }

   //////////////////////////////////////////////////////////////////////////
   // Non-public fields
   //////////////////////////////////////////////////////////////////////////

   /** The activity. */
   private ASKActivity m_activityContext;

   /** The list of the current user's friends. */
   private final List<String> m_friends = Collections.synchronizedList(new ArrayList<String>());

   /** The current user. */
   private ParseUser m_user;

   /** The adapter for the list of friends. */
   private FriendsListAdapter mAdapter;

   /** The search box. */
   private TextView mSearchBox;
}
