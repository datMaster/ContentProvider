package com.datmaster.contentprovider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private static EditText url;
		private static EditText title;
		private static EditText icon;
		private static Content content;
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			
			content = new Content(getActivity());
			
			ListView  lw = (ListView)rootView.findViewById(R.id.listView);
			Button saveButton = (Button)rootView.findViewById(R.id.buttonSave);			
			
			final ItemsAdapter adapter = new ItemsAdapter(getActivity());			
			lw.setAdapter(adapter);						
			
			url = (EditText)rootView.findViewById(R.id.EditTextURL);
			title = (EditText)rootView.findViewById(R.id.EditTextTitle);
			icon = (EditText)rootView.findViewById(R.id.editTextIcon);
			
			saveButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DBItem newItem = new DBItem();
					newItem.setUrl("http://" + url.getText().toString());
					newItem.setTitle(title.getText().toString());
					newItem.setIcon(loadIcon("/sdcard/Download/" + icon.getText().toString() + ".png"));
					content.addRecord(newItem);
					adapter.update();					
				}
			});
			
			return rootView;
		}
		
		private Bitmap loadIcon(String path) {
			Bitmap icon = BitmapFactory.decodeFile(path);		
			return icon;
		}
	}
}
