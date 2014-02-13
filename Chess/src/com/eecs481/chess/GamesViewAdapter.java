package com.eecs481.chess;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import ask.scanninglibrary.views.ASKAdapter;
import ask.scanninglibrary.ASKActivity;

public class GamesViewAdapter extends ASKAdapter<GameInfo> {
	
	public GamesViewAdapter(ASKActivity context, ListView listView) {
		super(context, R.layout.games_list_row, listView);
	}
	
	public GamesViewAdapter(ASKActivity context, int resource,
			int textViewResourceId, List<GameInfo> objects,
			AdapterView<?> view) {
		super(context, resource, textViewResourceId, objects, view);
	}
	
	public GamesViewAdapter(ASKActivity context, int resource,
			List<GameInfo> objects, AdapterView<?> view) {
		super(context, resource, objects, view);
	}

	public GamesViewAdapter(ASKActivity context, int resource,
			GameInfo[] objects, AdapterView<?> view) {
		super(context, resource, objects, view);
	}
	
	public GamesViewAdapter(ASKActivity context, int resource,
			AdapterView<?> view) {
		super(context, resource, view);
	}
	
	public GamesViewAdapter(ASKActivity context, int resource,
			int textViewResourceId, GameInfo[] objects, AdapterView<?> view) {
		super(context, resource, textViewResourceId, objects, view);
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
