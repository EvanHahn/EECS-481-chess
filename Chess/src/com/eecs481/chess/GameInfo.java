package com.eecs481.chess;

public class GameInfo {
	public GameInfo(String id_, String opponent_, String status_) {
		id = id_;
		opponent = opponent_;
		status = status_;
	}
	
	public final String id;
	public final String opponent;
	public final String status;
}
