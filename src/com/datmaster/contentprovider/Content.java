package com.datmaster.contentprovider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.Browser;

public class Content extends SQLiteOpenHelper {

	private SQLiteDatabase dataBase;
	
	private static final String BOOKMARKS_DIRECTORY = "/bookmarks/";
	private static String filePath; 
	
	public Content(Context context) {		
		super(context, ContentConstants.DB_NAME, null, ContentConstants.DB_VERSION);	
		filePath = Environment.getExternalStorageDirectory().getPath() + BOOKMARKS_DIRECTORY;
		File directory = new File(filePath);
		if(directory.exists() == false && directory.isDirectory() == false)
			directory.mkdirs();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {	
		db.execSQL(ContentConstants.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {		
	}
	
	private void insertNewRecord(ContentValues content) {
		dataBase.insert(ContentConstants.TABLE_NAME, null, content);
	}
	
	public void addRecord(DBItem newItem) {
		dataBase = getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put(Browser.BookmarkColumns.TITLE, newItem.getTitle());
		content.put(Browser.BookmarkColumns.URL, newItem.getUrl());
		String path = filePath + newItem.getIcon().toString() + ".png";		
		saveIcon(newItem.getIcon(), path);
		content.put(Browser.BookmarkColumns.FAVICON, path);
		insertNewRecord(content);
		dataBase.close();
		
	}
	
	public void removeRecord(int position) {
		dataBase = getReadableDatabase();
		removeFile(getFileName(position));
		dataBase.close();
		
		dataBase = getWritableDatabase();
		dataBase.execSQL("delete from " 
										+ ContentConstants.TABLE_NAME 
										+ " where " 
										+ Browser.BookmarkColumns._ID 
										+ "=' " 
										+ position 
										+ " '");
		dataBase.close();		
	}
	
	private String getFileName(int id) {
		String path = "";
		Cursor cursor =  dataBase.rawQuery("select * from " 
																	+ ContentConstants.TABLE_NAME 
																	+ " where " 
																	+ Browser.BookmarkColumns._ID 
																	+ "='" 
																	+ id 
																	+ "'" , null);
		try {
			if(cursor.moveToFirst()) {
				path = cursor.getString(cursor.getColumnIndex(Browser.BookmarkColumns.FAVICON));				
			}
		} catch (Exception e) {
		}
		cursor.close();		
		return path;
	}	
	
	public ArrayList<DBItem> getAllBookmarks() {

		dataBase = getReadableDatabase();
		ArrayList<DBItem> records = new ArrayList<DBItem>();

		Cursor cursor = dataBase.rawQuery(ContentConstants.SELECT_ALL_QUERY, null);

		if (cursor.moveToFirst()) {
			do {
				DBItem dbItem = new DBItem();
				dbItem.setId(cursor.getInt(cursor.getColumnIndex(Browser.BookmarkColumns._ID)));
				dbItem.setUrl(cursor.getString(cursor
						.getColumnIndex(Browser.BookmarkColumns.URL)));
				dbItem.setTitle(cursor.getString(cursor
						.getColumnIndex(Browser.BookmarkColumns.TITLE)));
				dbItem.setIcon(loadIcon((cursor.getString(cursor
						.getColumnIndex(Browser.BookmarkColumns.FAVICON)))));
				records.add(dbItem);
			} while (cursor.moveToNext());
		}

		cursor.close();
		dataBase.close();
		return records;
	}
	
	private Bitmap loadIcon(String path) {
		Bitmap icon = BitmapFactory.decodeFile(path);		
		return icon;
	}
	
	private void saveIcon(Bitmap bmp, String filename) {	    	    
		FileOutputStream fos = null;
	      try {
	        fos = new FileOutputStream(filename);
	        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
	      } catch (Exception e) {
	        e.printStackTrace();
	      } finally {
	        try {
	          fos.flush();
	          fos.close();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
    }
	
	private void removeFile(String path) {
		File file = new File(path);
		if(file != null)
			file.delete();	
	}

}
