package com.datmaster.contentprovider;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemsAdapter extends BaseAdapter implements OnClickListener{
	
	private LayoutInflater inflater;
	private ArrayList<DBItem> dataItems;
	private ArrayList<ViewHolder> listItems;
	private Content content;
	
	public ItemsAdapter(Context context) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		content = new Content(context);		
		dataItems = new ArrayList<>();
		listItems = new ArrayList<>();
		dataItems = content.getAllBookmarks();
		for(int i = 0; i < dataItems.size(); i ++) {
			listItems.add(new ViewHolder());
		}
	}
	
	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.list_item, null); 
		}
		
		ViewHolder holder = listItems.get(position);
		
		holder.logo = (ImageView)convertView.findViewById(R.id.imageViewLogoItem);
		holder.title = (TextView)convertView.findViewById(R.id.textViewTitleItem);
		holder.url = (TextView)convertView.findViewById(R.id.textViewUrlItem);
		
		holder.logo.setImageBitmap(dataItems.get(position).getIcon());
		holder.title.setText(dataItems.get(position).getTitle());
		holder.url.setText(dataItems.get(position).getUrl());
		
		holder.logo.setOnClickListener(this);
		holder.logo.setTag(position);
		
		return convertView;
	}

	@Override
	public void onClick(View v) {
		int position = Integer.parseInt(v.getTag().toString());
		content.removeRecord(dataItems.get(position).getId());
		dataItems.remove(position);
		listItems.remove(position);	
		notifyDataSetChanged();
	}
	
	public class ViewHolder {
		public TextView url;
		public TextView title;
		public ImageView logo;
	}
	
	public void update() {
		dataItems.clear();
		dataItems = content.getAllBookmarks();
		listItems.clear();
		for(int i = 0; i < dataItems.size(); i ++) {
			listItems.add(new ViewHolder());
		}
		notifyDataSetChanged();
	}
	
}
