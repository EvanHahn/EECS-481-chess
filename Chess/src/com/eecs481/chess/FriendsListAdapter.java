package com.eecs481.chess;

import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import ask.scanninglibrary.ASKActivity;
import ask.scanninglibrary.views.ASKAdapter;

/**
 * Custom adapter to support the displaying of a Graceful CHess user's friends.
 * 
 * @author Jake Korona
 * @author Will Wood
 */
public class FriendsListAdapter extends ASKAdapter<String> {

   //////////////////////////////////////////////////////////////////////////
   // Public methods
   //////////////////////////////////////////////////////////////////////////

   public FriendsListAdapter(ASKActivity context, ListView listView) {
      super(context, R.layout.games_list_row, listView);
      m_activityContext = context;
      m_inflater = (LayoutInflater) m_activityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   }


   public void setList(List<String> list) {
      m_friends = list;
   }

   public List<String> getList() {
      return m_friends;
   }

   /**
    * How many items are in the data set represented by this Adapter.
    * 
    * @return The total count.
    */
   @Override
   public int getCount() {
      return (m_friends == null ? 0 : m_friends.size());
   }

   @Override
   public String getItem(int position) {
      if (position < 0 || position >= getCount())
         throw new IllegalArgumentException("Invalid offset: " + position);

      return m_friends.get(position);
   }

   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public View askGetView(int position, View convertView, final ViewGroup parent) {
      View rowView = m_inflater.inflate(R.layout.friends_list_row, parent, false);

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

      TextView friendUsername = (TextView) rowView.findViewById(R.id.friend_username);
      friendUsername.setText(getItem(position));

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

   //////////////////////////////////////////////////////////////////////////
   // Non-public fields
   //////////////////////////////////////////////////////////////////////////

   /** The activity (context) for this adapter. */
   private final ASKActivity m_activityContext;

   /** The {@link LayoutInflater} for this adapter. */
   private final LayoutInflater m_inflater;

   private List<String> m_friends;

   private int mCurPage = 0;

}
