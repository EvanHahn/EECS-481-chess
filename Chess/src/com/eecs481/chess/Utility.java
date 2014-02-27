package com.eecs481.chess;

import java.util.ArrayList;

import com.parse.ParseObject;

public class Utility {
	public static ArrayList<String> getGameParams(ParseObject game) {
	    ArrayList<String> gameParams = new ArrayList<String>();    
	    gameParams.add(Consts.NETWORK);
	    gameParams.add(game.getObjectId());
	    gameParams.add(game.getString(Consts.P1_FIELD));
	    gameParams.add(game.getString(Consts.P2_FIELD));
	    gameParams.add(game.getString(Consts.STATUS_FIELD));
	    gameParams.add(game.getString(Consts.CUR_GAME_FIELD));
	    
	    return gameParams;
	}
	
	public static ArrayList<String> getGameParams() {
        ArrayList<String> gameParams = new ArrayList<String>();
        gameParams.add(Consts.PNP);
	    
	    return gameParams;
	}

}
