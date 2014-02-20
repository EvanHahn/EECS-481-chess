package com.eecs481.chess;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import ask.scanninglibrary.ASKActivity;
import ask.scanninglibrary.views.ASKAdapter;

/**
 * 
 * @author Jake Korona
 */
public class FriendsListAdapter extends ASKAdapter<String> {
   //////////////////////////////////////////////////////////////////////////
   // Public fields
   //////////////////////////////////////////////////////////////////////////

   //////////////////////////////////////////////////////////////////////////
   // Public methods
   //////////////////////////////////////////////////////////////////////////

   public FriendsListAdapter(ASKActivity context, int resource, AdapterView<?> view) {
      super(context, resource, view);

      if (context == null)
         throw new IllegalArgumentException("The context must not be null");

      m_activityContext = context;
      m_inflater = (LayoutInflater) m_activityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

   /**
    * Gets an item.
    * 
    * @param position The item to retrieve. Must be non-negative and less than the total number of items returned by
    *        {@link #getCount()}.
    * @return The item at {@code position}.
    */
   @Override
   public String getItem(int position) {
      if (position < 0 || position >= getCount())
         throw new IllegalArgumentException("Invalid offset: " + position);

      return m_friends.get(position);
   }

   /**
    * Gets the row id associated with the specified position in the list.
    * 
    * @param position The item to retrieve. Must be non-negative and less than
    *        the total number of items returned by {@link #getCount()}.
    * @return The row ID.
    */
   @Override
   public long getItemId(int position) {
      return position;
   }

   @Override
   public View askGetView(int position, View convertView, final ViewGroup parent) {
      //LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      String friendUsername = getItem(position);
      if (convertView == null) {
         m_holder = new ViewHolder();

         convertView = m_inflater.inflate(R.layout.friends_list_row, null);

         convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
               //Intent intent = new Intent(m_activityContext, GameActivity.class);
               //intent.putExtra(name, value);
               m_activityContext.startActivity(new Intent(m_activityContext, GameActivity.class));
            }
         });

         m_holder.username = (TextView) convertView.findViewById(R.id.friend_username);

         convertView.setTag(m_holder);
      }
      else {
         m_holder = (ViewHolder) convertView.getTag();
      }

      m_holder.username.setText(friendUsername);

      return convertView;
   }

   public void setList(List<String> list) {
      m_friends = list;
   }

   public List<String> getList() {
      return m_friends;
   }

   //////////////////////////////////////////////////////////////////////////
   // Non-public methods
   //////////////////////////////////////////////////////////////////////////

   //////////////////////////////////////////////////////////////////////////
   // Non-public fields
   //////////////////////////////////////////////////////////////////////////

   /** The activity (context) for this adapter. */
   private final ASKActivity m_activityContext;

   /** The {@link LayoutInflater} for this adapter. */
   private final LayoutInflater m_inflater;

   /** The {@link ViewHolder} for this adapter. */
   private ViewHolder m_holder;

   private List<String> m_friends;

   /** An optimization class. Minimizes the amount of work in the {@code getView()} method of the adapter. */
   private class ViewHolder {
      TextView username;
   }
}
