package com.eecs481.chess;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.ListView;
import android.widget.TextView;
import ask.scanninglibrary.views.ASKAdapter;
import ask.scanninglibrary.ASKActivity;

public class GamesViewAdapter extends ASKAdapter<String> {
	public GamesViewAdapter(ASKActivity context, ListView listView) {
		super(context, R.layout.games_list_row, listView);
	}
	
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		
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
		
		TextView textView = (TextView) rowView.findViewById(R.id.text_view);
		textView.setText(getItem(position));
		
		return rowView;
		
	}

}
