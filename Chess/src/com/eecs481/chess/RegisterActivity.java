package com.eecs481.chess;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ask.scanninglibrary.ASKActivity;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

/**
 * The registration screen activity.
 * 
 * @author Jake Korona
 */
public class RegisterActivity extends ASKActivity {

   //////////////////////////////////////////////////////////////////////////
   // Public fields
   //////////////////////////////////////////////////////////////////////////
   /** Missing username message text */
   public static String NO_USERNAME = "You didn't enter a username!";

   /** Missing password message text */
   public static String NO_PASSWORD = "You didn't enter a password!";

   //////////////////////////////////////////////////////////////////////////
   // Activity Overrides
   //////////////////////////////////////////////////////////////////////////

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.register_layout);

      Parse.initialize(this, "4SV3X5Flt3tqhr87pM29xI36jKYtUWnZWBBI70iH",
               "vxkrbGSjbstIO1UjUg6PGicEUCSvcEVo9p5kjgZ4");

      m_activityContext = this;

      ParseUser currentUser = ParseUser.getCurrentUser();
      if (currentUser != null) {
         Log.i("RegisterActivity", "Device is registered to: "
                  + currentUser.getUsername());

         m_activityContext.startActivity(
            new Intent(m_activityContext, Homescreen.class));
      }
      else {
         Button registerButton = (Button) findViewById(R.id.register_btn);
         registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onRegisterButtonClicked();
            }
         });

         Button loginButton = (Button) findViewById(R.id.login_btn);
         loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onLoginButtonClicked();
            }
         });
      }
   }

   @Override
   protected void onResume() {
      super.onResume();
      if (ParseUser.getCurrentUser() != null)
         m_activityContext.finish();
      else {
         EditText un = (EditText)findViewById(R.id.register_username);
         un.setText("");
         EditText pw = (EditText)findViewById(R.id.register_password);
         pw.setText("");
      }
   }

   //////////////////////////////////////////////////////////////////////////
   // Non-public methods
   //////////////////////////////////////////////////////////////////////////

   /**
    * Handler method for the registration button. Registers a user to Parse. If
    * registration is successful, the user is taken to the homescreen, else an
    * error message appears.
    */
   private void onRegisterButtonClicked() {
      EditText un = (EditText)findViewById(R.id.register_username);
      String username = un.getText().toString();
      EditText pw = (EditText)findViewById(R.id.register_password);
      String password = pw.getText().toString();

      if (username.isEmpty())
         makeToast(NO_USERNAME);
      else if (password.isEmpty())
         makeToast(NO_PASSWORD);
      else {
         ParseUser user = new ParseUser();
         user.setUsername(username);
         user.setPassword(password);

         user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException pe) {
               if (pe == null) {
                  m_activityContext.startActivity(
                     new Intent(m_activityContext, Homescreen.class));
               }
               else
                  makeToast(pe.getMessage());
            }
         });
      }
   }

   /**
    * Handler method for the login button. Logs a user in to the application. If
    * login is successful, the user is taken to the homescreen, else an
    * error message appears.
    */
   private void onLoginButtonClicked() {
      EditText un = (EditText)findViewById(R.id.register_username);
      String username = un.getText().toString();
      EditText pw = (EditText)findViewById(R.id.register_password);
      String password = pw.getText().toString();

      if (username.isEmpty())
         makeToast(NO_USERNAME);
      else if (password.isEmpty())
         makeToast(NO_PASSWORD);
      else {
         ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException pe) {
               if (user != null) {
                  m_activityContext.startActivity(
                     new Intent(m_activityContext, Homescreen.class));
               }
               else
                  makeToast(pe.getMessage());
            }
         });
      }
   }

   /**
    * Displays a standardized toast message.
    * 
    * @param message The message to display.
    */
   private void makeToast(String message) {
      Toast.makeText(m_activityContext, message, Toast.LENGTH_SHORT).show();
   }

   //////////////////////////////////////////////////////////////////////////
   // Non-public fields
   //////////////////////////////////////////////////////////////////////////

   /** The activity. */
   private Activity m_activityContext;
}
