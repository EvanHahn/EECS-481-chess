package com.eecs481.chess;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import ask.scanninglibrary.ASKActivity;



/*
 * THIS CLASS IS NOT PART OF THE BETA RELEASE, IT WILL BE USED IN THE FINAL RELEASE
 */

public class GameSettingsPnp extends ASKActivity {

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_game_settings_pnp);
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
      
      Button okButton = (Button) findViewById(R.id.okButton);
      Button cancelButton = (Button) findViewById(R.id.cancelButton);
      
      mWhiteScanCheckBox = (CheckBox)findViewById(R.id.whiteScanCheckBox);
      mBlackScanCheckBox = (CheckBox)findViewById(R.id.blackScanCheckBox);
      
      mWhiteScanCheckBox.setChecked(true);
      mBlackScanCheckBox.setChecked(false);
      
      okButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(GameSettingsPnp.this, GameActivity.class);
              boolean whiteScan = mWhiteScanCheckBox.isChecked();
              boolean blackScan = mBlackScanCheckBox.isChecked();
              //intent.putExtra(Consts.GAME_PARAMS, Utility.getGameParams(whiteScan, blackScan));
        	  startActivity(intent);
          }
       });
      
      cancelButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
        	  GameSettingsPnp.this.finish();
          }
       });

   }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.game_settings_pnp, menu);
		return true;
	}
	
	private CheckBox mWhiteScanCheckBox;
	private CheckBox mBlackScanCheckBox;

}

