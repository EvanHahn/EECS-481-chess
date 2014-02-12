package com.eecs481.chess;

import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import ask.scanninglibrary.ASKActivity;
import ask.scanninglibrary.views.ASKAlertDialog;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class Homescreen extends ASKActivity {

   public static ParseObject active_game;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_homescreen);
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//

      m_activityContext = this;
      m_gameList = new GameListManager(this);
      m_newGameButton = (Button) findViewById(R.id.newGameButton);

      m_newGameButton.setOnClickListener(new OnClickListener(){
         @Override
         public void onClick(View arg0) {
            final AutoCompleteTextView input = new AutoCompleteTextView(m_activityContext);
            input.setHint("Search for a player...");

            m_friends = getFriends();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(m_activityContext,
                     android.R.layout.simple_dropdown_item_1line, m_friends);
            input.setAdapter(adapter);

            ASKAlertDialog aad = new ASKAlertDialog(m_activityContext);
            aad.setTitle("New Game");
            aad.setView(input);
            aad.setButton(AlertDialog.BUTTON_POSITIVE, "Pass and Play", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int whichButton) {
                  startActivity(new Intent(Homescreen.this, GameActivity.class));
               }
            });
            //            aad.setButton(AlertDialog.BUTTON_NEGATIVE, "Search", new DialogInterface.OnClickListener() {
            //               @Override
            //               public void onClick(DialogInterface dialog, int whichButton) {
            //                  String name = input.getText().toString();
            //                  if (name.isEmpty())
            //                     return;
            //                  Log.i("Homescreen", "Searching for user: " + name);
            //
            //                  final List<String> userList = new ArrayList<String>();
            //                  ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
            //                  query.whereNotEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
            //                  query.whereEqualTo("username", name);
            //                  query.findInBackground(new FindCallback<ParseObject>() {
            //                     @Override
            //                     public void done(List<ParseObject> users, ParseException pe) {
            //                        if (pe == null) {
            //                           for (ParseObject user : users) {
            //                              Log.i("Homescreen", "Adding friend: " + user.getString("username"));
            //                              userList.add(user.getString("username"));
            //                           }
            //                        }
            //                        else {
            //                           Log.e("Homescreen", "Error: " + pe.getMessage());
            //                        }
            //                     }
            //                  });
            //
            //                  for (String un : userList)
            //                     Log.i("Homescreen", "Users: " + un);
            //
            //                  if (!userList.isEmpty()) {
            //                     Log.i("Homescreen", "Adding friend: " + name);
            //                     saveFriends(userList);
            //                  }
            //               }
            //            });
            aad.show();
         }
      });

   }

   @Override
   protected void onResume() {
      super.onResume();

      m_gameList.findUserGames();
   }

   @Override
   protected void onDestroy() {
      super.onDestroy();
   }


   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.homescreen_menu, menu);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.action_settings:
            return true;
         case R.id.action_logout:
            ParseUser.logOut();
            m_activityContext.finish();
            return true;
         default:
            return super.onOptionsItemSelected(item);
      }
   }

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

   private List<String> getFriends() {
      return ParseUser.getCurrentUser().getList("friends");
   }

   private List<String> m_friends;
   private ASKActivity m_activityContext;
   private Button m_newGameButton;
   private GameListManager m_gameList;
}
