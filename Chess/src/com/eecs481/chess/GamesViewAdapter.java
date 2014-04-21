package com.eecs481.chess;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import ask.scanninglibrary.ASKActivity;
import ask.scanninglibrary.views.ASKAdapter;

public class GamesViewAdapter extends ASKAdapter<GameInfo> {

   public GamesViewAdapter(ASKActivity context, ListView listView) {
      super(context, R.layout.games_list_row, listView);
   }

   @Override
   public View askGetView(int position, View convertView, final ViewGroup parent) {

      LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View rowView = inflater.inflate(R.layout.games_list_row, parent, false);

      rowView.setOnTouchListener(new OnTouchListener() {
         @Override
         public boolean onTouch(View arg0, MotionEvent arg1) {
            if (arg1.getAction() == MotionEvent.ACTION_UP) {
               ListView theView = ((ListView) parent);
               int pos = theView.getPositionForView(arg0);
               theView.performItemClick(theView, pos, theView.getItemIdAtPosition(pos));
            }
            return false;
         }
      });

      TextView textViewOpponent = (TextView) rowView.findViewById(R.id.glist_row_text_view1);
      TextView textViewStatus = (TextView) rowView.findViewById(R.id.glist_row_text_view2);

      textViewOpponent.setText(getItem(position).opponent);
      textViewStatus.setText(getItem(position).status);
      if (getItem(position).yourTurn)
         rowView.setBackgroundColor(Color.GREEN);
      else
         rowView.setBackgroundColor(Color.rgb(255, 119, 119));

      return rowView;
   }

   @Override
   public void nextPage() {
      if (getCount() > 0) {
         mCurPage++;
         super.nextPage();
      }
   }

   @Override
   public void prevPage() {
      if (mCurPage > 0) {
         mCurPage--;
         super.prevPage();
      }
   }

   private int mCurPage = 0;
}
