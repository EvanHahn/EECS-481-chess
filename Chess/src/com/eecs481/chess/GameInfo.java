package com.eecs481.chess;

import java.util.Comparator;

public class GameInfo {
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
      return arg1.status.compareToIgnoreCase(arg0.status);
   }
}