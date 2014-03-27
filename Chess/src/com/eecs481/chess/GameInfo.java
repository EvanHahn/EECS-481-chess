package com.eecs481.chess;

import java.util.Comparator;

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

class StatusComparator implements Comparator<GameInfo> {
   @Override
   public int compare(GameInfo arg0, GameInfo arg1) {
      return arg1.status.compareToIgnoreCase(arg0.status);
   }
}