package com.eecs481.chess;

import java.util.Comparator;

public class GameInfo {

   public static final String YOUR_TURN_WHITE_MSG = "White's Turn (You)";
	
   public static final String OPPONENTS_TURN_WHITE_MSG = "White's Turn (Them)";
	
   public static final String YOUR_TURN_BLACK_MSG = "Black's Turn (You)";
	
   public static final String OPPONENTS_TURN_BLACK_MSG = "Black's Turn (Them)";
	
   public static final String GAME_FINISHED_MSG = "Game Over";
	
   public GameInfo(String id_, String opponent_, String status_, boolean yourTurn_) {
      id = id_;
      opponent = opponent_;
      status = status_;
      yourTurn = yourTurn_;
   }

   public final String id;
   public final String opponent;
   public final String status;
   public final boolean yourTurn;
}

class StatusComparator implements Comparator<GameInfo> {
   @Override
   public int compare(GameInfo arg0, GameInfo arg1) {
	   if (arg0.status.equals(GameInfo.YOUR_TURN_WHITE_MSG) 
		|| arg0.status.equals(GameInfo.YOUR_TURN_BLACK_MSG)) {
		   
		   if (arg1.status.equals(GameInfo.YOUR_TURN_WHITE_MSG) 
			|| arg1.status.equals(GameInfo.YOUR_TURN_BLACK_MSG)) {
			   return 0;
		   } else {
			   return -1;
		   }
		   
	   } else if (arg0.status.equals(GameInfo.GAME_FINISHED_MSG)) {
		   if (arg1.status.equals(GameInfo.GAME_FINISHED_MSG)) {
			   return 0;
		   } else {
			   return 1;
		   }
		   
	   } else {
		   
		   if (arg1.status.equals(GameInfo.GAME_FINISHED_MSG)) {
			   return -1;
		   } else if (arg1.status.equals(GameInfo.OPPONENTS_TURN_WHITE_MSG) 
				|| arg1.status.equals(GameInfo.OPPONENTS_TURN_BLACK_MSG)) {
			   return 0;
		   } else {
			   return 1;
		   }
	   }
   }
}