package com.eecs481.chess;

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
import ask.scanninglibrary.ASKActivity;
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
      FriendsListAdapter adapter = new FriendsListAdapter(m_activityContext, R.layout.friends_list_row, friendsListView);
      adapter.setList(m_friends);
      friendsListView.setAdapter(adapter);

      friendsListView.setOnItemClickListener(new OnItemClickListener() {
         @Override
         public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            //Intent intent = new Intent(m_activityContext, GameActivity.class);
            //intent.putExtra(name, value);
            startActivity(new Intent(m_activityContext, GameActivity.class));
         }
      });

      //      ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
      //      query.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
      //      query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
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
